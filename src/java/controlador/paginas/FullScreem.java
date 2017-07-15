/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import entidades.BvArtigo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;

/**
 *
 * @author Almerindo Uazela
 */
public class FullScreem extends SelectorComposer<Component> {

    @Wire
    private Iframe reportframe;
    
    @Wire
    Component fullScreem;
    
    @Wire
    Image voltar;
    
    @Wire
    Button fechar;
    
    BvArtigo fileR;
    AMedia fileContent;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        fileR = new BvArtigo();

        final Execution execution = Executions.getCurrent();
        fileR = (BvArtigo) execution.getArg().get("artigo");

        abrirFull(fileR);
    }

    public void abrirFull(BvArtigo a) throws FileNotFoundException, IOException {
            
            File f = new File(a.getDirectorioPdf());
            byte[] buffer = new byte[(int) f.length()];
            FileInputStream fs = new FileInputStream(f);
            fs.read(buffer);
            fs.close();
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            String nome = a.getTitulo();
            fileContent = new AMedia(nome, "pdf", "application/pdf", is);
            reportframe.setContent(fileContent);
        }
    
    @Listen("onClick=#voltar")
    public void showModal(Event e) {
		fullScreem.detach();
                //Executions.getCurrent().sendRedirect("/Paginas/leitor/leituras.zul");
	}
    
    @Listen("onClick=#fechar")
    public void fechando(Event e) {
		fullScreem.detach();
                
	}
    }

