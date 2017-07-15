/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BReservaJpaController;
import controladores.entidades.UsersJpaController;
import entidades.BReserva;
import entidades.Users;
import java.util.Iterator;
import java.util.List;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Almerindo Uazela
 */
public class CircReserva extends SelectorComposer<Component>{
    @Wire
    Listbox reservaListbox;
    
    @Wire
    Textarea searchArea;
    
    ListModelList<BReserva> reservas;
    List<BReserva> todos;
    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
        
        //Carregando BReservas do Utilizador actual
        reservas = new ListModelList<BReserva>(new BReservaJpaController(new JPA().getEmf()).findBReservaEntities());
        reservaListbox.setModel(reservas);
    }
    
    @Listen("onClick=#nova1")
    public void irUtilixadores(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circulacao.zul");
    }
    
    @Listen("onClick=#nova")
    public void irSubmissoes(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circ_submissoes.zul");
    }
    
    @Listen("onClick=#nova2")
    public void manter(){
        Executions.getCurrent().sendRedirect("/Paginas/admin/circ_reservas.zul");
    }
    @Listen("onChanging=#searchArea")
    public void onChang(InputEvent event) {
       
        List<BReserva>todas = new BReservaJpaController(new JPA().getEmf()).findBReservaEntities();
        
        for (Iterator<BReserva> iterator = todas.iterator(); iterator.hasNext();) {
            BReserva value = iterator.next();
            if (value.getLivro().getObraRef().getTitulo().toLowerCase().contains(event.getValue().toLowerCase())) {
                
            } else {
                iterator.remove();            
            }
            
        }
        reservas= new ListModelList<BReserva>(todas);
        reservaListbox.setModel(reservas);
 
    }
}
