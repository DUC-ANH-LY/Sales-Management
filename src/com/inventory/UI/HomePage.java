/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inventory.UI;

import com.inventory.DAO.UserDAO;
import com.inventory.DTO.UserDTO;
import com.inventory.Database.ConnectionFactory;
import java.awt.*;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;

/**
 *
 * @author asjad
 */
// Welcome page for the application
public final class HomePage extends javax.swing.JPanel {

    /**
     * Creates new form HomePage
     */
    Statement statement = null;
    Connection conn = null;
    ResultSet resultSet = null;
//

    public HomePage(String username) {

        try {
            conn = new ConnectionFactory().getConn();
            statement = conn.createStatement();
            initComponents();
            showPieChart();
            showLineChart();
            update();
            UserDTO userDTO = new UserDTO();
            new UserDAO().getFullName(userDTO, username);
            welcomeLabel.setText("Welcome,  " + userDTO.getFullName() + ".");

        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void update() throws SQLException {
        conn = new ConnectionFactory().getConn();
        statement = conn.createStatement();
        try {
            String query = "select count(*) as totalSales from salesinfo";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                noSales.setText(resultSet.getString("totalSales"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            String query = "select count(*) as totalProducts from products where quantity > 0";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                noProduct.setText(resultSet.getString("totalProducts"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            String query = "select count(*) as totalCus from customers";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                noCustomer.setText(resultSet.getString("totalCus"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            String query = "select count(*) as totalUs from users";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                noUser.setText(resultSet.getString("totalUs"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        showPieChart();
        showLineChart();
    }

    public void showPieChart() {

        //create dataset
        DefaultPieDataset barDataset = new DefaultPieDataset();

        try {
            String query = "select productcode, count(*) * 100.0 / (select count(*) from salesinfo) as percentage from salesinfo group by productcode";
            resultSet = statement.executeQuery(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            while (resultSet.next()) {
                String prodCode = null;
                try {
                    prodCode = resultSet.getString("productcode");
                } catch (SQLException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
                String percentage = null;
                try {
                    percentage = resultSet.getString("percentage");
                } catch (SQLException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
                barDataset.setValue(prodCode, Double.valueOf(percentage));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }

        //create chart
        JFreeChart piechart = ChartFactory.createPieChart("Sales PieChart", barDataset, false, true, false);//explain

        PiePlot piePlot = (PiePlot) piechart.getPlot();

        //changing pie chart blocks colors
        piePlot.setSectionPaint("IPhone 5s", new Color(255, 255, 102));
        piePlot.setSectionPaint("SamSung Grand", new Color(102, 255, 102));
        piePlot.setSectionPaint("MotoG", new Color(255, 102, 153));
        piePlot.setSectionPaint("Nokia Lumia", new Color(0, 204, 204));

        piePlot.setBackgroundPaint(Color.white);

        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        PanePie.removeAll();
        PanePie.add(barChartPanel, BorderLayout.CENTER);
        PanePie.validate();
    }

    /*=============================================================================*/
    public void showLineChart() throws SQLException {
        try {
            String query = "SELECT SUBSTRING(date, 5, 3) AS month,count(*) as totalSales,\n"
                    + "case \n"
                    + " when SUBSTRING(date, 5, 3) ='Jan' then 1\n"
                    + "when SUBSTRING(date, 5, 3) ='Feb' then 2\n"
                    + " when SUBSTRING(date, 5, 3) ='Mar' then 3\n"
                    + "when SUBSTRING(date, 5, 3) ='Apr' then 4\n"
                    + "when SUBSTRING(date, 5, 3) ='May' then 5\n"
                    + " when SUBSTRING(date, 5, 3) ='Jun' then 6\n"
                    + " when SUBSTRING(date, 5, 3) ='Jul' then 7\n"
                    + " when SUBSTRING(date, 5, 3) ='Aug' then 8\n"
                    + " when SUBSTRING(date, 5, 3) ='Sep' then 9\n"
                    + " when SUBSTRING(date, 5, 3) ='Oct' then 10\n"
                    + " when SUBSTRING(date, 5, 3) ='Nov' then 11\n"
                    + "else  12\n"
                    + "end as month_num from salesinfo  group by month order by month_num";
            resultSet = statement.executeQuery(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        while (resultSet.next()) {
            String month = null;
            try {
                month = resultSet.getString("month");
            } catch (SQLException ex) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
            }
            String totalSales = null;
            try {
                totalSales = resultSet.getString("totalSales");
            } catch (SQLException ex) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
            }
            dataset.setValue(Integer.parseInt(totalSales), "Total", month);
        }

        //create dataset for the graph
//        dataset.setValue(200, "Amount", "january");
//        dataset.setValue(150, "Amount", "february");
//        dataset.setValue(18, "Amount", "march");
//        dataset.setValue(100, "Amount", "april");
//        dataset.setValue(80, "Amount", "may");
//        dataset.setValue(250, "Amount", "june");
//        dataset.setValue(250, "Amount", "june");
//                        dataset.setValue(250, "Amount", "june");
//        dataset.setValue(250, "Amount", "june");
//        dataset.setValue(250, "Amount", "june");
        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Sales LineChart", "Monthly", "Total",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        PaneLine.removeAll();
        PaneLine.add(lineChartPanel, BorderLayout.CENTER);
        PaneLine.validate();
    }

//    /*========================================================================================*/
//    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        welcomeLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        PanePie = new javax.swing.JPanel();
        PaneLine = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        noCustomer = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        noSales = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        noProduct = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        noUser = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        welcomeLabel.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        welcomeLabel.setText("Welcome!");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanePie.setLayout(new java.awt.BorderLayout());
        jPanel1.add(PanePie, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 380, 320));

        PaneLine.setLayout(new java.awt.BorderLayout());
        jPanel1.add(PaneLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 270, 590, 320));

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        noCustomer.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        noCustomer.setText("10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addComponent(noCustomer)
                .addGap(81, 81, 81))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addComponent(noCustomer))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 240, 90));

        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        noSales.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        noSales.setText("10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(noSales)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(noSales)
                .addGap(0, 7, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 230, 90));

        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        noProduct.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        noProduct.setText("10");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(noProduct)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noProduct)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, 220, 90));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setText("No of Customers");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, 20));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel2.setText("No of Products");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel5.setText("No of Sales");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));

        noUser.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        noUser.setText("10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(noUser)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(noUser)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 40, 200, 100));

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel3.setText("No of User");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 10, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1052, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(365, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 667, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        try {
            // TODO add your handling code here:
            update();
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_formComponentShown

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PaneLine;
    private javax.swing.JPanel PanePie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel noCustomer;
    private javax.swing.JLabel noProduct;
    private javax.swing.JLabel noSales;
    private javax.swing.JLabel noUser;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
