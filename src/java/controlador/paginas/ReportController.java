/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;
import static conexao.ReportConection.getRelatorioConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

/**
 *
 * @author Migueljr
 */
public class ReportController extends SelectorComposer<Component>{
    
    
    @Wire
    Button gerar;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onClickSair(){
    
    }
    
    public void onClickImprimir(){
    
    }
    
    @Listen("onClick=#gerar")
    public void onClickGerarReport() throws SQLException, JRException{
        gerarReport();
    }
    
    public void gerarReport() throws SQLException, JRException{
        
//        Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bh", "postgres", "0000");
        
        String report = "C:\\Users\\Migueljr\\Documents\\NetBeansProjects\\BHi\\web\\relatorios\\Obras.jasper";
//        JasperReport jasperReport = JasperCompileManager.compileReport(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, getRelatorioConnection());
//        JasperViewer.viewReport(jasperPrint);
        JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
        jrviewer.setVisible(true);  
    }
    
    
    @Listen("onClick=#gerarU")
    
    
    public void gerarReportU() throws SQLException, JRException{
        
//        Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bh", "postgres", "0000");
        
        String report = "C:\\Users\\Migueljr\\Documents\\NetBeansProjects\\BHi\\web\\relatorios\\report3.jasper";
//        JasperReport jasperReport = JasperCompileManager.compileReport(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, getRelatorioConnection());
//        JasperViewer.viewReport(jasperPrint);
        JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
        jrviewer.setVisible(true);  
    }
    
     @Listen("onClick=#gerarE")
    
    
    public void gerarReportE() throws SQLException, JRException{
        
//        Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bh", "postgres", "0000");
        
        String report = "C:\\Users\\Migueljr\\Documents\\NetBeansProjects\\BHi\\web\\relatorios\\emprestimos.jasper";
//        JasperReport jasperReport = JasperCompileManager.compileReport(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, getRelatorioConnection());
//        JasperViewer.viewReport(jasperPrint);
        JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
        jrviewer.setVisible(true);  
    }
         @Listen("onClick=#gerarM")
     public void gerarReportM() throws SQLException, JRException{
        
//        Connection conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bh", "postgres", "0000");
        
        String report = "C:\\Users\\Migueljr\\Documents\\NetBeansProjects\\BHi\\web\\relatorios\\multa.jasper";
//        JasperReport jasperReport = JasperCompileManager.compileReport(report);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, getRelatorioConnection());
//        JasperViewer.viewReport(jasperPrint);
        JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
        jrviewer.setVisible(true);  
    }

}
