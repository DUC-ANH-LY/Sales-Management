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
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author asjad
 */
// Welcome page for the application
public final class HomePage extends javax.swing.JPanel {

    /**
     * Creates new form HomePage
     */
//    Statement statement = null;
//    Connection conn = null;
//    ResultSet resultSet = null;
//
//    conn  = new ConnectionFactory().getConn();
//    statement  = conn.createStatement();

    public HomePage(String username) {
        initComponents();
//        showPieChart();
        UserDTO userDTO = new UserDTO();
        new UserDAO().getFullName(userDTO, username);
        welcomeLabel.setText("Welcome,  " + userDTO.getFullName() + ".");
    }

//    public void showPieChart() {
//
//        //create dataset
//        DefaultPieDataset barDataset = new DefaultPieDataset();
//
//        try {
//            String query = "select productcode, count(*) * 100.0 / (select count(*) from salesinfo) as percentage from salesinfo group by productcode";
//            resultSet = statement.executeQuery(query);
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        try {
//            while (resultSet.next()) {
//                String prodCode = null;
//                try {
//                    prodCode = resultSet.getString("productcode");
//                } catch (SQLException ex) {
//                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String percentage = null;
//                try {
//                    percentage = resultSet.getString("percentage");
//                } catch (SQLException ex) {
//                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                barDataset.setValue(prodCode, Double.valueOf(percentage));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        //create chart
//        JFreeChart piechart = ChartFactory.createPieChart("Sales Info", barDataset, false, true, false);//explain
//
//        PiePlot piePlot = (PiePlot) piechart.getPlot();
//
//        //changing pie chart blocks colors
//        piePlot.setSectionPaint("IPhone 5s", new Color(255, 255, 102));
//        piePlot.setSectionPaint("SamSung Grand", new Color(102, 255, 102));
//        piePlot.setSectionPaint("MotoG", new Color(255, 102, 153));
//        piePlot.setSectionPaint("Nokia Lumia", new Color(0, 204, 204));
//
//        piePlot.setBackgroundPaint(Color.white);
//
//        //create chartPanel to display chart(graph)
//        ChartPanel barChartPanel = new ChartPanel(piechart);
//        panelBarChart.removeAll();
//        panelBarChart.add(barChartPanel, BorderLayout.CENTER);
//        panelBarChart.validate();
//    }

    /*=============================================================================*/
//    public void showLineChart(){
//        //create dataset for the graph
//         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.setValue(200, "Amount", "january");
//        dataset.setValue(150, "Amount", "february");
//        dataset.setValue(18, "Amount", "march");
//        dataset.setValue(100, "Amount", "april");
//        dataset.setValue(80, "Amount", "may");
//        dataset.setValue(250, "Amount", "june");
//        
//        //create chart
//        JFreeChart linechart = ChartFactory.createLineChart("contribution","monthly","amount", 
//                dataset, PlotOrientation.VERTICAL, false,true,false);
//        
//        //create plot object
//         CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
//       // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
//        lineCategoryPlot.setBackgroundPaint(Color.white);
//        
//        //create render object to change the moficy the line properties like color
//        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
//        Color lineChartColor = new Color(204,0,51);
//        lineRenderer.setSeriesPaint(0, lineChartColor);
//        
//         //create chartPanel to display chart(graph)
//        ChartPanel lineChartPanel = new ChartPanel(linechart);
//        panelBarChart.removeAll();
//        panelBarChart.add(lineChartPanel, BorderLayout.CENTER);
//        panelBarChart.validate();
//    }
//
//    /*========================================================================================*/
//    
//    public void showHistogram(){
//        
//         double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
//                            12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
//                            49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
//                            93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
//                            77, 44, 58, 91, 10, 67, 57, 19, 88, 84                                
//                          };
// 
// 
//        HistogramDataset dataset = new HistogramDataset();
//        dataset.addSeries("key", values, 20);
//        
//         JFreeChart chart = ChartFactory.createHistogram("JFreeChart Histogram","Data", "Frequency", dataset,PlotOrientation.VERTICAL, false,true,false);
//            XYPlot plot= chart.getXYPlot();
//        plot.setBackgroundPaint(Color.WHITE);
//
//        
//        
//        ChartPanel barpChartPanel2 = new ChartPanel(chart);
//        jPanel3.removeAll();
//        jPanel3.add(barpChartPanel2, BorderLayout.CENTER);
//        jPanel3.validate();
//    }
//
//    /*========================================================================================*/
//    
//    public void showBarChart(){
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.setValue(200, "Amount", "january");
//        dataset.setValue(150, "Amount", "february");
//        dataset.setValue(18, "Amount", "march");
//        dataset.setValue(100, "Amount", "april");
//        dataset.setValue(80, "Amount", "may");
//        dataset.setValue(250, "Amount", "june");
//        
//        JFreeChart chart = ChartFactory.createBarChart("contribution","monthly","amount", 
//                dataset, PlotOrientation.VERTICAL, false,true,false);
//        
//        CategoryPlot categoryPlot = chart.getCategoryPlot();
//        //categoryPlot.setRangeGridlinePaint(Color.BLUE);
//        categoryPlot.setBackgroundPaint(Color.WHITE);
//        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
//        Color clr3 = new Color(204,0,51);
//        renderer.setSeriesPaint(0, clr3);
//        
//        ChartPanel barpChartPanel = new ChartPanel(chart);
//        panelBarChart.removeAll();
//        panelBarChart.add(barpChartPanel, BorderLayout.CENTER);
//        panelBarChart.validate();
//        
//        
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        welcomeLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelBarChart = new javax.swing.JPanel();

        welcomeLabel.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        welcomeLabel.setText("Welcome");

        jLabel1.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html>Manage  your  inventory,  transactions  and  personnel,  all  in  one place.<br><br>Click  on  the  Menu  button  to  start.<html>");

        panelBarChart.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(panelBarChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(452, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBarChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(419, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelBarChart;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
