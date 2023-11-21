/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inventory.DAO;

import com.inventory.DTO.ProductDTO;
import com.inventory.Database.ConnectionFactory;
import java.awt.Image;
import java.io.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author asjad
 */
// Data Access Object for Products, Purchase, Stock and Sales
public class ProductDAO {

    Connection conn = null;
    PreparedStatement prepStatement = null;
    PreparedStatement prepStatement2 = null;
    Statement statement = null;
    Statement statement2 = null;
    ResultSet resultSet = null;

    public ProductDAO() {
        try {
            conn = new ConnectionFactory().getConn();
            statement = conn.createStatement();
            statement2 = conn.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getSuppInfo() {
        try {
            String query = "SELECT * FROM suppliers";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getCustInfo() {
        try {
            String query = "SELECT * FROM customers";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdStock() {
        try {
            String query = "SELECT * FROM currentstock";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdInfo() {
        try {
            String query = "SELECT * FROM products";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Double getProdCost(String prodCode) {
        Double costPrice = null;
        try {
            String query = "SELECT costprice FROM products WHERE productcode='" + prodCode + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                costPrice = resultSet.getDouble("costprice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return costPrice;
    }

    public Double getProdSell(String prodCode) {
        Double sellPrice = null;
        try {
            String query = "SELECT sellprice FROM products WHERE productcode='" + prodCode + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                sellPrice = resultSet.getDouble("sellprice");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sellPrice;
    }

    String suppCode;

    public String getSuppCode(String suppName) {
        try {
            String query = "SELECT suppliercode FROM suppliers WHERE fullname='" + suppName + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                suppCode = resultSet.getString("suppliercode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppCode;
    }

    String prodCode;

    public String getProdCode(String prodName) {
        try {
            String query = "SELECT productcode FROM products WHERE productname='" + prodName + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                suppCode = resultSet.getString("productcode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodCode;
    }

    String custCode;

    public String getCustCode(String custName) {
        try {
            String query = "SELECT customercode FROM suppliers WHERE fullname='" + custName + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                suppCode = resultSet.getString("customercode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return custCode;
    }

    // Method to check for availability of stock in Inventory
    boolean flag = false;

    public boolean checkStock(String prodCode) {
        try {
            String query = "SELECT * FROM currentstock WHERE productcode='" + prodCode + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    // Methods to add a new product
    public void addProductDAO(ProductDTO productDTO) throws FileNotFoundException {
        try {
            String query = "SELECT * FROM products WHERE productname='"
                    + productDTO.getProdName()
                    + "' AND costprice='"
                    + productDTO.getCostPrice()
                    + "' AND sellprice='"
                    + productDTO.getSellPrice()
                    + "' AND brand='"
                    + productDTO.getBrand()
                    + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Product has already been added.");
            } else {
                addFunction(productDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFunction(ProductDTO productDTO) throws FileNotFoundException {
        try {
            String query = "INSERT INTO products VALUES(?,?,?,?,?,?,?,?)";
            prepStatement = (PreparedStatement) conn.prepareStatement(query);
            prepStatement.setString(1, productDTO.getProdCode());
            prepStatement.setString(2, productDTO.getProdName());
            prepStatement.setDouble(3, productDTO.getCostPrice());
            prepStatement.setDouble(4, productDTO.getSellPrice());
            prepStatement.setString(5, productDTO.getBrand());
            prepStatement.setBlob(6, new FileInputStream(new File(productDTO.getImage())));
            prepStatement.setInt(7, productDTO.getQuantity());
            prepStatement.setString(8, productDTO.getDate());

            String query2 = "INSERT INTO currentstock VALUES(?,?)";
            prepStatement2 = conn.prepareStatement(query2);
            prepStatement2.setString(1, productDTO.getProdCode());
            prepStatement2.setInt(2, productDTO.getQuantity());

            prepStatement.executeUpdate();
            prepStatement2.executeUpdate();
            JOptionPane.showMessageDialog(null, "Product added and ready for sale.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Method to add a new purchase transaction
    public void addPurchaseDAO(ProductDTO productDTO) {
        try {
            String query = "INSERT INTO purchaseinfo VALUES(?,?,?,?,?)";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, productDTO.getSuppCode());
            prepStatement.setString(2, productDTO.getProdCode());
            prepStatement.setString(3, productDTO.getDate());
            prepStatement.setInt(4, productDTO.getQuantity());
            prepStatement.setDouble(5, productDTO.getTotalCost());

            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Purchase log added.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String prodCode = productDTO.getProdCode();
        if (checkStock(prodCode)) {
            try {
                String query = "UPDATE currentstock SET quantity=quantity+? WHERE productcode=?";
                prepStatement = conn.prepareStatement(query);
                prepStatement.setInt(1, productDTO.getQuantity());
                prepStatement.setString(2, prodCode);

                prepStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (!checkStock(prodCode)) {
            try {
                String query = "INSERT INTO currentstock VALUES(?,?)";
                prepStatement = (PreparedStatement) conn.prepareStatement(query);
                prepStatement.setString(1, productDTO.getProdCode());
                prepStatement.setInt(2, productDTO.getQuantity());

                prepStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        deleteStock();
    }

    // Method to update existing product details
    public void editProdDAO(ProductDTO productDTO) throws FileNotFoundException {
        try {
            String query;
            System.out.println(productDTO.getImage());
            if (productDTO.getImage() == null || productDTO.getImage().equals("") || productDTO.getImage().contains("sun.awt.image")) {
                query = "UPDATE products SET productname=?,costprice=?,sellprice=?,brand=?,quantity=?,added_date=? WHERE productcode=?";
                prepStatement = (PreparedStatement) conn.prepareStatement(query);
                prepStatement.setString(1, productDTO.getProdName());
                prepStatement.setDouble(2, productDTO.getCostPrice());
                prepStatement.setDouble(3, productDTO.getSellPrice());
                prepStatement.setString(4, productDTO.getBrand());
                prepStatement.setString(7, productDTO.getProdCode());
                prepStatement.setInt(5, productDTO.getQuantity());
                System.out.println(productDTO.getDate());
                prepStatement.setString(6, productDTO.getDate());

            } else {
                query = "UPDATE products SET productname=?,costprice=?,sellprice=?,brand=?,image=?,quantity=?,added_date=? WHERE productcode=?";
                prepStatement = (PreparedStatement) conn.prepareStatement(query);
                prepStatement.setString(1, productDTO.getProdName());
                prepStatement.setDouble(2, productDTO.getCostPrice());
                prepStatement.setDouble(3, productDTO.getSellPrice());
                prepStatement.setString(4, productDTO.getBrand());
                prepStatement.setString(8, productDTO.getProdCode());
                prepStatement.setBlob(5, new FileInputStream(new File(productDTO.getImage())));
                prepStatement.setInt(6, productDTO.getQuantity());
                prepStatement.setString(7, productDTO.getDate());
            }

            String query2 = "UPDATE currentstock SET quantity=? WHERE productcode=?";
            prepStatement2 = conn.prepareStatement(query2);
            prepStatement2.setInt(1, productDTO.getQuantity());
            prepStatement2.setString(2, productDTO.getProdCode());

            prepStatement.executeUpdate();
            prepStatement2.executeUpdate();
            JOptionPane.showMessageDialog(null, "Product details updated.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Methods to handle updating of stocks in Inventory upon any transaction made
    public void editPurchaseStock(String code, int quantity) {
        try {
            String query = "SELECT * FROM currentstock WHERE productcode='" + code + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String query2 = "UPDATE currentstock SET quantity=quantity-? WHERE productcode=?";
                prepStatement = conn.prepareStatement(query2);
                prepStatement.setInt(1, quantity);
                prepStatement.setString(2, code);
                prepStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void editSoldStock(String code, int quantity) {
        try {
            String query = "SELECT * FROM currentstock WHERE productcode='" + code + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String query2 = "UPDATE currentstock SET quantity=quantity+? WHERE productcode=?";
                prepStatement = conn.prepareStatement(query2);
                prepStatement.setInt(1, quantity);
                prepStatement.setString(2, code);
                prepStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteStock() {
        try {
            String query = "DELETE FROM currentstock WHERE productcode NOT IN(SELECT productcode FROM purchaseinfo)";
            String query2 = "DELETE FROM salesinfo WHERE productcode NOT IN(SELECT productcode FROM products)";
            statement.executeUpdate(query);
            statement.executeUpdate(query2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Method to permanently delete a product from inventory
    public void deleteProductDAO(String code) {
        try {
            String query = "DELETE FROM products WHERE productcode=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, code);

            String query2 = "DELETE FROM currentstock WHERE productcode=?";
            prepStatement2 = conn.prepareStatement(query2);
            prepStatement2.setString(1, code);

            prepStatement.executeUpdate();
            prepStatement2.executeUpdate();

            JOptionPane.showMessageDialog(null, "Product has been removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteStock();
    }

    public void deletePurchaseDAO(int ID) {
        try {
            String query = "DELETE FROM purchaseinfo WHERE purchaseID=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            prepStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Transaction has been removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteStock();
    }

    public void deleteSaleDAO(int ID) {
        try {
            String query = "DELETE FROM salesinfo WHERE salesID=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, ID);
            prepStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Transaction has been removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteStock();
    }

    // Sales transaction handling
    public void sellProductDAO(ProductDTO productDTO, String username) {
        int quantity = 0;
        String prodCode = null;
        try {
            String query = "SELECT * FROM products WHERE productcode='" + productDTO.getProdCode() + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                prodCode = resultSet.getString("productcode");
                quantity = resultSet.getInt("quantity");
            }
            if(quantity <= 0)   JOptionPane.showMessageDialog(null, "Product sold out!");
            if (productDTO.getQuantity() > quantity) {
                JOptionPane.showMessageDialog(null, "This quantity greater than available for this product.");
            } else if (productDTO.getQuantity() <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a valid quantity");
            } else {
                String stockQuery = "UPDATE products SET quantity=quantity-'"
                        + productDTO.getQuantity()
                        + "' WHERE productcode='"
                        + productDTO.getProdCode()
                        + "'";
                String salesQuery = "INSERT INTO salesinfo(date,productcode,customercode,quantity,revenue,soldby)"
                        + "VALUES('" + productDTO.getDate() + "','" + productDTO.getProdCode() + "','" + productDTO.getCustCode()
                        + "','" + productDTO.getQuantity() + "','" + productDTO.getTotalRevenue() + "','" + username + "')";
                statement.executeUpdate(stockQuery);
                statement.executeUpdate(salesQuery);
                JOptionPane.showMessageDialog(null, "Product sold.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Products data set retrieval for display
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT productcode,productname,costprice,sellprice,fullname,quantity,added_date,image FROM products JOIN suppliers ON products.brand = suppliers.fullname ORDER BY productcode";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    // Purchase table data set retrieval
    public ResultSet getPurchaseInfo() {
        try {
            String query = "SELECT PurchaseID,purchaseinfo.ProductCode,ProductName,purchaseinfo.Quantity,Totalcost "
                    + "FROM purchaseinfo INNER JOIN products "
                    + "ON products.productcode=purchaseinfo.productcode ORDER BY purchaseid;";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

  

    // Sales table data set retrieval
    public ResultSet getSalesInfo() {
        try {
            String query = """
                    SELECT salesid,salesinfo.productcode,productname,
                    salesinfo.quantity,revenue,users.name AS Sold_by
                    FROM salesinfo INNER JOIN products
                    ON salesinfo.productcode=products.productcode
                    INNER JOIN users
                    ON salesinfo.soldby=users.username;
                    """;
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    // Search method for products
    public ResultSet getProductSearch(String text) {
        try {
            String query = "SELECT productcode,productname,costprice,sellprice,fullname,quantity,added_date,image FROM products join suppliers on products.spid = suppliers.sid "
                    + "WHERE productcode LIKE '%" + text + "%' OR productname LIKE '%" + text + "%' OR brand LIKE '%" + text + "%'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdFromCode(String text) {
        try {
            String query = "SELECT productcode,productname,costprice,sellprice,brand FROM products "
                    + "WHERE productcode='" + text + "' LIMIT 1";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Search method for sales
    public ResultSet getSalesSearch(String text) {
        try {
            String query = "SELECT salesid,salesinfo.productcode,productname,\n"
                    + "                    salesinfo.quantity,revenue,users.name AS Sold_by\n"
                    + "                    FROM salesinfo INNER JOIN products\n"
                    + "                    ON salesinfo.productcode=products.productcode\n"
                    + "                    INNER JOIN users\n"
                    + "                    ON salesinfo.soldby=users.username\n"
                    + "                    INNER JOIN customers\n"
                    + "                    ON customers.customercode=salesinfo.customercode\n"
                    + "WHERE salesinfo.productcode LIKE '%" + text + "%' OR productname LIKE '%" + text + "%' "
                    + "OR users.name LIKE '%" + text + "%' OR customers.fullname LIKE '%" + text + "%' ORDER BY salesid;";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Search method for purchase logs
    public ResultSet getPurchaseSearch(String text) {
        try {
            String query = "SELECT PurchaseID,purchaseinfo.productcode,products.productname,quantity,totalcost "
                    + "FROM purchaseinfo INNER JOIN products ON purchaseinfo.productcode=products.productcode "
                    + "INNER JOIN suppliers ON purchaseinfo.suppliercode=suppliers.suppliercode"
                    + "WHERE PurchaseID LIKE '%" + text + "%' OR productcode LIKE '%" + text + "%' OR productname LIKE '%" + text + "%' "
                    + "OR suppliers.fullname LIKE '%" + text + "%' OR purchaseinfo.suppliercode LIKE '%" + text + "%' "
                    + "OR date LIKE '%" + text + "%' ORDER BY purchaseid";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getProdName(String code) {
        try {
            String query = "SELECT * FROM products WHERE productcode='" + code + "'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public String getSuppName(int ID) {
        String name = null;
        try {
            String query = "SELECT fullname FROM suppliers "
                    + "INNER JOIN purchaseinfo ON suppliers.suppliercode=purchaseinfo.suppliercode "
                    + "WHERE purchaseid='" + ID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                name = resultSet.getString("fullname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
    }

    public String getCustName(int ID) {
        String name = null;
        try {
            String query = "SELECT fullname FROM customers "
                    + "INNER JOIN salesinfo ON customers.customercode=salesinfo.customercode "
                    + "WHERE salesid='" + ID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                name = resultSet.getString("fullname");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
    }

    public String getPurchaseDate(int ID) {
        String date = null;
        try {
            String query = "SELECT date FROM purchaseinfo WHERE purchaseid='" + ID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                date = resultSet.getString("date");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return date;
    }

    public String getSaleDate(int ID) {
        String date = null;
        try {
            String query = "SELECT date FROM salesinfo WHERE salesid='" + ID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                date = resultSet.getString("date");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return date;
    }

    // Method to display product-related data set in tabular form
//    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
//        ResultSetMetaData metaData = resultSet.getMetaData();
//        Vector<String> columnNames = new Vector<String>();
//        int colCount = metaData.getColumnCount();
//
//        for (int col=1; col <= colCount; col++){
//            columnNames.add(metaData.getColumnName(col).toUpperCase(Locale.ROOT));
//        }
//
//        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
//        while (resultSet.next()) {
//            Vector<Object> vector = new Vector<Object>();
//            int col;
//            for (col=1; col<=colCount-1; col++) {
//                vector.add(resultSet.getObject(col));
//            }
//            vector.add(resultSet.getBytes("image"));
//            data.add(vector);
//        }
//        return new DefaultTableModel(data, columnNames);
//    }
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colCount = metaData.getColumnCount();

        Vector<String> columnNames = new Vector<>(colCount);

        for (int col = 1; col <= colCount; col++) {
            if (metaData.getColumnName(col).equalsIgnoreCase("fullname")) {
                columnNames.add("suppliers".toUpperCase());
            } else if (metaData.getColumnName(col).equalsIgnoreCase("added_date")) {
                columnNames.add("date".toUpperCase());
            } else {
                columnNames.add(metaData.getColumnName(col).toUpperCase(Locale.ROOT));
            }

        }

        Vector<Vector<Object>> data = new Vector<>();

        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>(colCount);

            for (int col = 1; col <= colCount; col++) {
                if (metaData.getColumnName(col).equalsIgnoreCase("image")) {
                    // Handle BLOB data appropriately
                    byte[] imageData = resultSet.getBytes(col);
                    if (imageData != null) {
                        ImageIcon imageIcon = new ImageIcon(imageData);
                        vector.add(imageIcon);
                    } else {
                        vector.add(null); // Handle null image data if necessary
                    }
                } else {
                    vector.add(resultSet.getObject(col));
                }
            }

            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

}
