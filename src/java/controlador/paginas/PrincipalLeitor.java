/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BReservaJpaController;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.BvLeituraJpaController;
import controladores.entidades.UsersJpaController;
import controladores.entidades.exceptions.IllegalOrphanException;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.BLeitor;
import entidades.BReserva;
import entidades.BvArtigo;
import entidades.BvLeitura;
import entidades.SgEmprestimo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Almerindo Uazela
 */
public class PrincipalLeitor extends SelectorComposer<Component> {

    @Wire
    private Listbox artigoListbox, reservaListbox, multaListbox, emprestimoListbox;

    @Wire
    Label recentes;

    @Wire
    Panel rec, res, mul, poss;

    @Wire
    Window principal;

    Component fullScreem;

    Autenticacao authService = new AutenticacaoImpl();
    BLeitor currentUser = new BLeitor();

    List<BReserva> todas = null;
    List<BvArtigo> todos = null;
    ListModelList<BvArtigo> artigos;
    ListModelList<BReserva> reservas;
    ListModelList<SgEmprestimo> emprestimos;
    ListModelList<SgEmprestimo> multas;

    BReserva reserva;
    BvArtigo artigo;
    BvLeitura leitura;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        //Pegando Utilizador actual
        UserCredential cre = authService.getUserCredential();
       // this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount()).getBLeitor();

        //Abrindo novos Artigos
        publicacaoRecentes();
        reservaPendente();
        multasPorpagar();
        emprestimosPendentes();

        Thread threadInfo = new Thread() {
            @Override
            public void run() {
                try {
                    chekin();
                } catch (Exception ex) {
//                    Logger.getLogger(Reservas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

    }

    public void multasPorpagar() {
        int i = 0;
//        List<SgEmprestimo> emp = currentUser.getSgEmprestimoList();
List<SgEmprestimo> emp = (List<SgEmprestimo>) currentUser.getSgEmprestimoCollection();
        for (Iterator<SgEmprestimo> iterator = emp.iterator(); iterator.hasNext();) {
            SgEmprestimo value = iterator.next();
            if (value.getMultaEstado() != null) {
                if ("Não paga".equals(value.getMultaEstado())) {
                    i++;
                } else {
                    iterator.remove();
                }
            }

        }
        if(i==0){
            
        }else{
        poss.setSclass("panel panel-danger");
        poss.setTitle("Dívidas (" + i + ")");
        multas = new ListModelList<SgEmprestimo>(emp);
        multaListbox.setModel(multas);
        }
    }

    public void emprestimosPendentes() {
        Date hoje = new Date();
        int i = 0;
//        List<SgEmprestimo> emp = currentUser.getSgEmprestimoList();
List<SgEmprestimo> emp = (List<SgEmprestimo>) currentUser.getSgEmprestimoCollection();
        for (Iterator<SgEmprestimo> iterator = emp.iterator(); iterator.hasNext();) {
            SgEmprestimo value = iterator.next();
            if (value.getEstado() != null) {
                if ("Pendente".equals(value.getEstado())) {
                    i++;
                } else {
                    iterator.remove();
                }
            }

        }
        if (i==0) {
 
        } else {
            poss.setSclass("panel panel-warning");
            poss.setTitle("Por Devolver(" + i + ")");
            emprestimos = new ListModelList<SgEmprestimo>(emp);
            emprestimoListbox.setModel(emprestimos);
        }

    }

    public void publicacaoRecentes() {

        todos = new BvArtigoJpaController(new JPA().getEmf()).findBvArtigoEntities();
        Date hoje = new Date();
        int i = 0;
        for (Iterator<BvArtigo> iterator = todos.iterator(); iterator.hasNext();) {
            BvArtigo value = iterator.next();
            if (value.getEstado().equals("Publicado") && (value.getDataPublicacao().getDay() == hoje.getDay() && value.getDataPublicacao().getMonth() == hoje.getMonth() && value.getDataPublicacao().getYear() == hoje.getYear())) {
                i++;
            } else {
                iterator.remove();
            }
        }
        if (i==0) {
           // rec.setVisible(false);

        } else {
            rec.setSclass("panel panel-primary");
            rec.setTitle("Publicaçoes Recentes (" + i + ")");
            artigos = new ListModelList<BvArtigo>(todos);
            artigoListbox.setModel(artigos);
        }

    }

    public void reservaPendente() {
        int i = 0;
        Date hoje = new Date();
//        todas = currentUser.getBReservaList();
todas = (List<BReserva>) currentUser.getBReservaCollection();
        
        for (Iterator<BReserva> iterator = todas.iterator(); iterator.hasNext();) {
            BReserva value = iterator.next();
            if (value.getEstado().equals("Pendente")) {
                i++;
            } else {
                iterator.remove();
            }
        }
        if (i==0) {
            //res.setVisible(false);

        } else {
            res.setSclass("panel panel-primary");
            res.setTitle("Reservas Pendentes (" + i + ")");
            reservas = new ListModelList<BReserva>(todas);
            reservaListbox.setModel(reservas);
        }
    }

    @Listen("onAbrir = #artigoListbox")
    public void abrirBvArtigo(ForwardEvent evt) throws FileNotFoundException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();

        artigo = (BvArtigo) litem.getValue();

        leitura = new BvLeitura(new Date(), artigo.getIdartigo(), new Date(), currentUser.getNrCartao());
        try {
            new BvLeituraJpaController(new JPA().getEmf()).create(leitura);
        } catch (Exception ex) {
            //Logger.getLogger(Leituras.class.getName()).log(Level.SEVERE, null, ex);
        }
        HashMap<String, BvArtigo> args = new HashMap<String, BvArtigo>();
        args.put("artigo", artigo);
        Window window = (Window) Executions.getCurrent().createComponents("fullScreem.zul", fullScreem, args);
        window.doHighlighted();
    }

    @Listen("onCancelar=#reservaListbox")
    public void removerPublicacao(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent().getParent();

        reserva = (BReserva) litem.getValue();
        try {
            reserva.setEstado("Cancelada");
            new BReservaJpaController(new JPA().getEmf()).edit(reserva);
            Clients.showNotification("Reserva Cancelada");
            doAfterCompose(principal);
        } catch (IllegalOrphanException ex) {
            Clients.showNotification("!Erro ao cancelar a reserva", true);

        } catch (NonexistentEntityException ex) {
//            Logger.getLogger(Publicacoes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void chekin() throws Exception {
        while (true) {
            for (BReserva r : currentUser.getBReservaCollection()) {
                if (r.getDataEmprestimo().before(new Date()) && r.getEstado().equals("Pendente")) {
                    r.setEstado("Expirada");
                    new BReservaJpaController(new JPA().getEmf()).edit(r);
                }
            }
            Thread.sleep(3600000); //a cada 1 horas verifica o estado da reserva (86400000 = 1dia) 
        }

    }

}
