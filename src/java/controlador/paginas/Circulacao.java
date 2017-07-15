/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BLeitorJpaController;
import controladores.entidades.BReservaJpaController;
import controladores.entidades.BvArtigoJpaController;

import entidades.BLeitor;
import entidades.BReserva;
import entidades.BvArtigo;

import java.util.Iterator;
import java.util.List;
import org.zkoss.zhtml.Textarea;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Almerindo Uazela
 */
public class Circulacao extends SelectorComposer<Component>{
    @Wire
    Listbox userListbox,reservaListbox,artigoListbox;
    
    @Wire
    Tab tabr, tabp;
    
    
    @Wire
    Textarea searchArea;
    
    @Wire
    Window userView;
    
    @Wire
    Vbox filtro;
    
    @Wire
    Button block;
    
    @Wire
    Div off,on;
    
    @Wire
    Label nome, posicao, cartao, conta;
    
    
    
    
    Component parent;
    ListModelList<BLeitor> users;
    ListModelList<BReserva> reservas;
    ListModelList<BvArtigo> artigos;
    
    //Autenticacao authService = new AutenticacaoImpl();
    //Users currentUser = new Users();
    BLeitor user;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        //UserCredential cre = authService.getUserCredential();
        //this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
        
        users = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        userListbox.setModel(users);
    }
    

    
    @Listen("onVer=#userListbox")
    public void verConta(ForwardEvent evt) throws Exception {
        
        
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();

        user = (BLeitor) litem.getValue();
        off.setVisible(false);
        on.setVisible(true);
        
        
        
        cartao.setValue(user.getNrCartao().toString());
        nome.setValue(user.getIdutilizador().getNome());
        conta.setValue(user.getTipoLeitor());
        
        if(user.getIdutilizador().getIdEstudante()!=null){
        
          posicao.setValue("Estudante - "+user.getIdutilizador().getIdEstudante().getCursocurrente().getDescricao());
        }
        if(user.getIdutilizador().getIdFuncionario()!=null){
          if(user.getIdutilizador().getIdFuncionario().getDocente()!=null){
              posicao.setValue("Docente");
          }else{
              posicao.setValue("CTA");
          }
        }
        
        
        
        
        
        reservas = new ListModelList<BReserva>(minhasBReservas(user));
        reservaListbox.setModel(reservas);
        
        artigos = new ListModelList<BvArtigo>(minhasPublicacoes(user));
        artigoListbox.setModel(artigos);
        
        

    }
    
    @Listen("onClick=#block")
    public void bloquearUser() throws Exception{
        if(user.getEstado().equals("Inactivo")){
            user.setEstado("Activo");
            new BLeitorJpaController(new JPA().getEmf()).edit(user);
            
            Clients.showNotification("O Utilizador "+ user.getNome()+ " foi activado!!", false);
        }else{
        user.setEstado("Inactivo");
        new BLeitorJpaController(new JPA().getEmf()).edit(user);
        Clients.showNotification("O Utilizador "+ user.getNome()+ " foi desativado", false);
        }
        
            
        
            
    }
    
    public List <BReserva> minhasBReservas(BLeitor u){
        int i=0;
        List<BReserva> todos = new BReservaJpaController(new JPA().getEmf()).findBReservaEntities();
        for (Iterator<BReserva> iterator = todos.iterator(); iterator.hasNext();) {
            BReserva value = iterator.next();
            if (!(value.getLeitor() == null ? u != null : !value.getLeitor().equals(u))) {
            i++;
            } else {
                iterator.remove();
            }
        }
        tabr.setLabel("Reservas ("+i+")" );
        return todos; 
    }
    
        public List<BvArtigo> minhasPublicacoes(BLeitor u) {
        int i=0;
        List<BvArtigo>todos = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigoEntities();
        for (Iterator<BvArtigo> iterator = todos.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (!(value.getPublicador() == null ? u != null : !value.getPublicador().equals(u))) {
            i++;
            } else {
                iterator.remove();
            }
        }
        tabp.setLabel("Submissões ("+i+")" );
        return todos;

    }
    
    
    
    
    public List<BLeitor> activos(){
        List<BLeitor> todos = new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities();
        for (Iterator<BLeitor> iterator = todos.iterator(); iterator.hasNext();) {
            BLeitor value = iterator.next();
            if (value.getEstado().equals("Activo")) {
            } else {
                iterator.remove();
            }
        }

        return todos;
    }
        public List<BLeitor> inativos(){
        List<BLeitor> todos = new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities();
        for (Iterator<BLeitor> iterator = todos.iterator(); iterator.hasNext();) {
            BLeitor value = iterator.next();
            if (value.getEstado().equals("Inactivo")) {
            } else {
                iterator.remove();
            }
        }

        return todos;
    }
    
    @Listen("onChanging=#searchArea")
    public void onChang(InputEvent event) {
       
        List<BLeitor> activos = new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities();

        
        for (Iterator<BLeitor> iterator = activos.iterator(); iterator.hasNext();) {
            BLeitor value = iterator.next();
            if (value.getIdutilizador().getNome().toLowerCase().contains(event.getValue().toLowerCase())) {
                
            } else {
                iterator.remove();            
            }
            
        }
        users= new ListModelList<BLeitor>(activos);
        userListbox.setModel(users);
 
    }
    
    @Listen("onClick=#nova")
    public void reservasGestor(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/administracao.zul");
    }
    
    @Listen("onClick=#nova2")
    public void submissoesGestor(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circ_submissoes.zul");
    }
    
    @Listen("onClick=#voltar")
    public void listare(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circulacao.zul");
    }
    
    @Listen("onClick=#nova1")
    public void listar(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circulacao.zul");
    }
    
}
