/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BvArtigoCategoriaJpaController;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.SgObraAreaJpaController;
import controladores.entidades.UsersJpaController;
import entidades.BLeitor;
import entidades.BvArtigo;
import entidades.BvArtigoCategoria;
import entidades.SgObraArea;
import entidades.Users;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModelSet;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.Info;
import servicos.UserCredential;

/**
 *
 * @author Uazela
 */
public class PublicacaoAdd extends SelectorComposer<Component> {

    @Wire
    Textbox autor, titulo, descricao, direitos;

    @Wire
    Button up,avaliador;

    @Wire
    private Label lbl;

    @Wire
    private Listbox area, categorias, lingua;

    ListModelList<BvArtigoCategoria> categoria;
    ListModelList<SgObraArea> areas;

    private String filePath;
    private boolean fileuploaded = false;
    private Media media;

    Autenticacao authService = new AutenticacaoImpl();
    BLeitor currentUser = new BLeitor();

    public boolean isFileuploaded() {
        return fileuploaded;
    }

    public void setFileuploaded(boolean fileuploaded) {
        this.fileuploaded = fileuploaded;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();

//        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getBLeitor();
        
        if(this.currentUser.getBvAvaliador()!=null){
            avaliador.setVisible(true);
            avaliador.setClass("btn-default");
            avaliador.setImage("/icon/ic_format_list_bulleted_black_18dp.png");
        }
        filePath = new String();

        categoria = new ListModelList<BvArtigoCategoria>(new BvArtigoCategoriaJpaController(new JPA().getEmf()).findBvArtigoCategoriaEntities());
        areas = new ListModelList<SgObraArea>(new SgObraAreaJpaController(new JPA().getEmf()).findSgObraAreaEntities());

        categorias.setModel(categoria);
        area.setModel(areas);

        ListModelList<String> linguaModel = new ListModelList<String>(Info.getIdiomaList());
        lingua.setModel(linguaModel);
    }

    @Listen("onClick=#submeter")
    public void submeterArtigo() {
        BvArtigoJpaController cBvArtigo = new BvArtigoJpaController(new JPA().getEmf());
        BvArtigo artigo = new BvArtigo();

        artigo.setAutor(autor.getValue());
        artigo.setTitulo(titulo.getValue());
        artigo.setDirectorioPdf(filePath);
     artigo.setPublicador(currentUser);
        artigo.setDataSubmissao(new Date());
        

        artigo.setFormato(media.getFormat());
        artigo.setDescricao(descricao.getValue());
        artigo.setDireitos(direitos.getValue());
        artigo.setEstado("Pendente");
        
        Set<String> ling = ((ListModelList) lingua.getModel()).getSelection();
        if(!ling.isEmpty()){
           artigo.setIdioma(ling.iterator().next()); 
        }else{
            artigo.setIdioma(null);
        }

        Set<SgObraArea> sele = ((ListModelList) area.getModel()).getSelection();
        if (!sele.isEmpty()) {
            artigo.setArea(sele.iterator().next());

        } else {
            artigo.setArea(null);
        }

        Set<BvArtigoCategoria> selection = ((ListModelList) categorias.getModel()).getSelection();
        if (!selection.isEmpty()) {
            artigo.setTipodoc(selection.iterator().next());
        } else {
            artigo.setTipodoc(null);
        }

        try {
           cBvArtigo.create(artigo);
            Clients.showNotification("Artigo submetido com sucesso");
            Executions.getCurrent().sendRedirect("/BV/Paginas/leitor/publicacaoList.zul");

        } catch (Exception ex) {
            Clients.showNotification("Erro na submissao do artigo");
        }

    }

    @Listen("onClick=#nova")
    public void minhasReservas() {
        Executions.getCurrent().sendRedirect("/BV/Paginas/leitor/publicacaoList.zul");
    }
    
    @Listen("onClick=#avaliador")
    public void avaliat(){
        
        Executions.getCurrent().sendRedirect("/BV/Paginas/leitor/publicacaoList.zul");
    }

    @Listen("onUpload=#up")
    public void carregarPDF(UploadEvent upEvent) throws IOException {

        lbl.setValue("" + upEvent.getMedia());

        media = upEvent.getMedia();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH); // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);

        filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
        String yearPath = "\\" + "REPOSITORIO_DE_ARTIGOS" + "\\" + year + "\\" + month + "\\" + day + "\\";
        filePath = filePath + yearPath;
        File baseDir = new File(filePath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        Files.copy(new File(filePath + media.getName()), media.getStreamData());
        //Messagebox.show("File Sucessfully uploaded in the path [ ." + filePath + " ]");
        fileuploaded = true;
        filePath = filePath + media.getName();

    }
}
