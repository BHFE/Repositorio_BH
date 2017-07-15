/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Relatorios;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import static conexao.ReportConection.getRelatorioConnection;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

/**
 *
 * @author Migueljr
 */
public class Emprestimos extends SelectorComposer<Component>{
    
       @Wire
    Datebox dataRela;

    @Wire
    Iframe relatorio;
    
    @Wire
    Button gerar;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        //realRelatorio();
    }

    @Listen("onClick=#gerar")
    public void realRelatorio() throws JRException, SQLException {
        String tste = "Paginas/relatorios/RelatorioEmprestimos.jasper";

        HashMap parametro = new HashMap();
        parametro.put("data_reserva", dataRela.getValue());

        String realtst = Executions.getCurrent().getDesktop().getWebApp().getRealPath(tste);
        File reportFile = new File(realtst);
        JasperReport relaJasper = (JasperReport) JRLoader.loadObject(reportFile);

        JasperPrint printJasper = JasperFillManager.fillReport(relaJasper, parametro, getRelatorioConnection());
        final AMedia media = new AMedia("Reservas", "pdf", "application/pdf", JasperExportManager.exportReportToPdf(printJasper));
        relatorio.setContent(media);

    }
}
