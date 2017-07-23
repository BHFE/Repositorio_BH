/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.BvAvaliadorJpaController;
import controladores.entidades.BvLeituraJpaController;
import controladores.entidades.UsersJpaController;
import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.BLeitor;

import entidades.BvArtigo;
import entidades.BvAvaliador;
import entidades.BvLeitura;
import entidades.Users;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
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
public class Publicacoes extends SelectorComposer<Component> {

    @Wire
    Button nova;

    @Wire
    private Listbox artigoListbox1;

    @Wire
    Textbox searchArea;

    @Wire
    Window publicacoesView;

    @Wire
    Button avaliador, lista;

    @Wire
    Div minhasPub, avaliacao,eu;
    
    @Wire
            Panel minhas;

    Component parent;

    ListModelList<BvArtigo> artigos;

    BvArtigo artigo;
    Autenticacao authService = new AutenticacaoImpl();
    BLeitor currentUser = new BLeitor();

    List<BvArtigo> todos;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        //Identificando o utilizador actual
        UserCredential cre = authService.getUserCredential();
//        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getBLeitorList();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getBLeitor();
        
        if(this.currentUser.getBvAvaliador()!=null){
            avaliador.setVisible(true);
            minhasPub.setVisible(false);
            avaliacao.setVisible(true);
            lista.setClass("btn btn-default");

            
        }
        
        
            
        

        //Preenchedo Tabelas
        artigos = new ListModelList<BvArtigo>(minhasPublicacoes());
        artigoListbox1.setModel(artigos);

    }

    public List<BvArtigo> minhasPublicacoes() {
        int i=0;
        todos = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigoEntities();
        for (Iterator<BvArtigo> iterator = todos.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if ((!(value.getPublicador() == null ? this.currentUser != null : !value.getPublicador().equals(this.currentUser))) && value.getEstado().equals("Publicado")) {
            i++;
            } else {
                iterator.remove();
            }
        }
        minhas.setTitle("Minhas Publicações ("+i+")");
       
        return todos;

    }

    public void destruirBvLeituras(BvArtigo artigo) {

        List<BvLeitura> leituras = new BvLeituraJpaController(new JPA().getEmf()).findBvLeituraEntities();
        for (BvLeitura leitura : leituras) {
            if (leitura.getBvArtigo().getIdartigo() == artigo.getIdartigo()) {
                try {
                    new BvLeituraJpaController(new JPA().getEmf()).destroy(leitura.getBvLeituraPK());
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Publicacoes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Listen("onRemover=#artigoListbox1")
    public void removerPublicacao(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent().getParent();

        artigo = (BvArtigo) litem.getValue();

        destruirBvLeituras(artigo);

        try {
            new BvArtigoJpaController(new JPA().getEmf()).destroy(artigo.getIdartigo());
            Clients.showNotification("Publicacao Removida");
            doAfterCompose(publicacoesView);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Publicacoes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Publicacoes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Listen("onEditar=#artigoListbox1")
    public void editarPublicacao(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent().getParent();
        artigo = (BvArtigo) litem.getValue();

        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("artigo", artigo);
        Window window = (Window) Executions.getCurrent().createComponents("publicacaoEdit.zul", parent, args);
        window.doHighlighted();
    }

    @Listen("onChanging=#searchArea")
    public void onChang(InputEvent event) {

        List<BvArtigo> todas = minhasPublicacoes();

        for (Iterator<BvArtigo> iterator = todas.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (value.getTitulo().toLowerCase().contains(event.getValue().toLowerCase()) || value.getAutor().toLowerCase().contains(event.getValue().toLowerCase())) {

            } else {
                iterator.remove();
            }

        }
        artigos = new ListModelList<BvArtigo>(todas);
        artigoListbox1.setModel(artigos);

    }

    @Listen("onClick=#lista")
    public void avaliarpage() {

        avaliacao.setVisible(false);
        minhasPub.setVisible(true);
        avaliador.setClass("btn btn-default");
        lista.setClass("btn btn-primary");

    }

    @Listen("onClick=#avaliador")
    public void avaliadorPage() {

        avaliacao.setVisible(true);
        minhasPub.setVisible(false);
        avaliador.setClass("btn btn-primary");
        lista.setClass("btn btn-default");

    }
    @Listen("onClick=#voltar")
    public void irsubmissao(){
        Executions.getCurrent().sendRedirect("/BV/Paginas/leitor/publicacaoList.zul");
    }

    @Listen("onClick=#nova")
    public void abrirForm() {
        Executions.getCurrent().sendRedirect("/BV/Paginas/leitor/publicacaoAdd.zul");

    }

}
