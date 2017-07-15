/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.UsersJpaController;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.BvArtigo;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Almerindo Uazela
 */
public class CircSubmissoes extends SelectorComposer<Component> {

    @Wire
    private Listbox artigoListbox;
    
    @Wire
    Label autor, titulo, descricao, direitos, idioma, area, categorias, formato, submissao, publicador, estado;
    
    @Wire
    Textbox searchArea;
    
    @Wire
    Div off,on;
    
    @Wire
    Window submiss;
    
    @Wire
    Panel panelon;
    
    @Wire
    Button rejeitar, publicar, voltar;
    
    Component fullScreem;
    
    ListModelList<BvArtigo> artigos;
    List<BvArtigo> todos;
    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();
    BvArtigo a;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        //Identificando o utilizador actual
        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
        
        //Preenchedo Tabelas
        todos = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigoEntities();
        artigos = new ListModelList<BvArtigo>(todos);
        artigoListbox.setModel(artigos);

    }
    @Listen("onChanging=#searchArea")
    public void onChang(InputEvent event) {
       
        List <BvArtigo> todas = todos;
        
        
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
        @Listen("onEditar=#artigoListbox")
    public void editarPublicacao(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        
        a = (BvArtigo) litem.getValue();
        off.setVisible(false);
        on.setVisible(true);
        
       
        if(a.getEstado().equals("Rejeitado") ){
            estado.setSclass("label label-danger");
            //rejeitar.setVisible(false);
             // publicar.setVisible(false);
              rejeitar.setDisabled(true);
              publicar.setDisabled(true);
              
        }else{
            if(a.getEstado().equals("Publicado")){
              estado.setSclass("label label-success");
              rejeitar.setDisabled(true);
              publicar.setDisabled(true);
              
            }else{
                estado.setSclass("label label-primary");
                panelon.setTitle("Avaliação da Submissão");
                voltar.setLabel("Adiar");
            }
        }
        
        
        titulo.setValue(a.getTitulo());
        autor.setValue(a.getAutor());
        descricao.setValue(a.getDescricao());
        direitos.setValue(a.getDireitos());
        idioma.setValue(a.getIdioma());
        submissao.setValue(a.getDataPublicacao().toString());
        publicador.setValue(a.getPublicador().getNome());
        formato.setValue(a.getFormato());
        categorias.setValue(a.getTipodoc().getCategoria());
        area.setValue(a.getArea().getDescricao());
        estado.setValue(a.getEstado());
        
        
        

       
    }
    
    @Listen("onClick=#abrir")
    public void abrirFile(){
        
        HashMap<String, BvArtigo>  args = new HashMap <String, BvArtigo>();
        args.put("artigo", a);
        Window window = (Window) Executions.getCurrent().createComponents("fullScreem.zul", fullScreem, args);
        window.doHighlighted();
    }

    
    
    
    @Listen("onClick=#nova1")
    public void irUtilixadores(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circulacao.zul");
    }
    
    @Listen("onClick=#nova2")
    public void ircirculacao(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circ_submissoes.zul");
    }
    
    @Listen("onClick=#voltar")
    public void irsubmissao(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circ_submissoes.zul");
    }
    
    @Listen("onClick=#nova")
    public void aqui(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/administracao.zul");
    }
    
    @Listen("onClick=#avaliar")
    public void avaliar(){
        Executions.getCurrent().sendRedirect("/Paginas/avaliador/avaliar_submissao.zul");
    }
    
   
    
    
    
    
}
