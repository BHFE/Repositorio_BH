 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BLeitorJpaController;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.BvAvaliadorJpaController;
import controladores.entidades.BvLeituraJpaController;
import controladores.entidades.UsersJpaController;
import entidades.BLeitor;
import entidades.BvArtigo;
import entidades.BvAvaliador;
import entidades.BvLeitura;
import entidades.Users;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Almerindo Uazela
 */
public class Leituras extends SelectorComposer<Component> {

    @Wire
    private Listbox artigoListbox;

    @Wire
    private Iframe reportframe;

    @Wire
    private Vbox conteudo;

    @Wire
    public Label actual;

    Component fullScreem;

    @Wire
    Textbox searchArea;

    @Wire
    Div desc;

    @Wire
    Label descricao, direitos;

    @Wire
    Image searchImg;

    AMedia fileContent;

    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }

    ListModelList<BvArtigo> artigos;
    BvArtigo selectedBvArtigo;

    BvArtigo artigo, parametro;

    BvLeitura leitura;

    Autenticacao authService = new AutenticacaoImpl();
    //Users currentUser = new Users();
    BLeitor currentUser = new BLeitor();
    List<BvArtigo> todos;
    Component fullS;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getBLeitor();

        todos = arigosValidos();

        leitura = ultimaBvLeitura();
        if (leitura == null) {
            actual.setClass("label label-primary");
            actual.setFocus(true);
            actual.setValue("Não foi possivel carregar a última leitura");

        } else {
            
                abrirInit(leitura);
            

        }

        artigos = new ListModelList<BvArtigo>(todos);
        artigoListbox.setModel(artigos);

    }

    public List<BvArtigo> arigosValidos() {
        List<BvArtigo> validos = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigoEntities();
        List<BvArtigo> ordenado = null;

        for (Iterator<BvArtigo> iterator = validos.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (value.getEstado().equals("Publicado")) {

            } else {
                iterator.remove();
            }

        }
        return validos;

    }

    @Listen("onChanging=#searchArea")
    public void onChang(InputEvent event) {

        List<BvArtigo> todas = arigosValidos();

        for (Iterator<BvArtigo> iterator = todas.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (value.getTitulo().toLowerCase().contains(event.getValue().toLowerCase()) || value.getAutor().toLowerCase().contains(event.getValue().toLowerCase())) {

            } else {
                iterator.remove();
            }

        }
        artigos = new ListModelList<BvArtigo>(todas);
        artigoListbox.setModel(artigos);

    }

    @Listen("onClick= #searchImg")
    public void procurar() {

        List<BvArtigo> todas = arigosValidos();

        for (Iterator<BvArtigo> iterator = todas.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (value.getTitulo().toLowerCase().contains(searchArea.getValue().toLowerCase()) || value.getAutor().toLowerCase().contains(searchArea.getValue().toLowerCase())) {

            } else {
                iterator.remove();
            }

        }
        artigos = new ListModelList<BvArtigo>(todas);
        artigoListbox.setModel(artigos);

    }

    public void abrirInit(BvLeitura ultima) throws FileNotFoundException, IOException {

        BvArtigo a = new BvArtigo();
        a = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigo(ultima.getBvLeituraPK().getObra());
        //Abrindo Pdf
        File f = new File(a.getDirectorioPdf());
        byte[] buffer = new byte[(int) f.length()];
        FileInputStream fs = new FileInputStream(f);
        fs.read(buffer);
        fs.close();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        String nome = a.getTitulo();
        fileContent = new AMedia(nome, "pdf", "application/pdf", is);
        reportframe.setContent(fileContent);
        actual.setValue(a.getTitulo());
        actual.setClass("label label-info");
        if (a.getDescricao() != null && a.getDireitos() != null) {
            desc.setVisible(true);
            descricao.setValue(a.getDescricao());

            direitos.setValue(a.getDireitos());

            direitos.setFocus(true);

        }

    }

    public BvLeitura ultimaBvLeitura() {
        BvLeitura ultima = new BvLeitura();
        int i = 0;
        boolean nenhuma = true;

        List<BvLeitura> todas = new BvLeituraJpaController(new JPA().getEmf()).findBvLeituraEntities();
        for (BvLeitura toda : todas) {

            if (toda.getBvLeituraPK().getLeitor() == currentUser.getNrCartao()) {
                if (i == 0) {
                    ultima = toda;
                } else {
                    if (toda == null) {
                        return ultima;
                    } else {
                        if (ultima.getBvLeituraPK().getDataLeitura().compareTo(toda.getBvLeituraPK().getDataLeitura()) == 0) {
                            if (ultima.getBvLeituraPK().getHorasLeitura().getTime() < toda.getBvLeituraPK().getHorasLeitura().getTime()) {
                                ultima = toda;
                            }
                        } else {
                            if (ultima.getBvLeituraPK().getDataLeitura().compareTo(toda.getBvLeituraPK().getDataLeitura()) < 0) {
                                ultima = toda;
                            }
                        }
                    }
                    nenhuma = false;
                }
                i++;
            }
        }
        if (nenhuma) {
            ultima = null;
        }

        return ultima;
    }

    @Listen("onAbrir = #artigoListbox")
    public void abrirBvArtigo(ForwardEvent evt) throws FileNotFoundException, IOException {
        Listcell btn = (Listcell) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent();

        artigo = (BvArtigo) litem.getValue();


        //Abrindo Pdf
        String filePath = artigo.getDirectorioPdf();
        File f = new File(filePath);
        //Messagebox.show(" dfdfdfdsfdsf" + filePath);
        byte[] buffer = new byte[(int) f.length()];
        FileInputStream fs = new FileInputStream(f);
        fs.read(buffer);
        fs.close();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        String nome = artigo.getTitulo();
        fileContent = new AMedia(nome, "pdf", "application/pdf", is);
        actual.setClass("label label-info");
        actual.setValue(artigo.getTitulo());

        if (artigo.getDescricao() != null && artigo.getDireitos() != null) {
            desc.setVisible(true);
            descricao.setValue(artigo.getDescricao());

            direitos.setValue(artigo.getDireitos());

            direitos.setFocus(true);

        }

        leitura = new BvLeitura(new Date(), artigo.getIdartigo(), new Date(), currentUser.getNrCartao());
        try {
            new BvLeituraJpaController(new JPA().getEmf()).create(leitura);
        } catch (Exception ex) {
            Logger.getLogger(Leituras.class.getName()).log(Level.SEVERE, null, ex);
        }

        reportframe.setContent(fileContent);

    }

    @Listen("onClick = #fullBtn")
    public void fullScream() {
        BvLeitura ultima = new BvLeitura();
        parametro = artigo;
        if (parametro == null) {
            ultima = ultimaBvLeitura();
            parametro = ultima.getBvArtigo();
        }
        HashMap<String, BvArtigo> args = new HashMap<String, BvArtigo>();
        args.put("artigo", parametro);
        Window window = (Window) Executions.getCurrent().createComponents("fullScreem.zul", fullScreem, args);
        window.doHighlighted();

    }

}
