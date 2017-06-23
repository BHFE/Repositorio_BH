/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.SgEmprestimoJpaController;
import controladores.entidades.SgExemplarJpaController;
import controladores.entidades.UsersJpaController;
import entidades.SgEmprestimo;
import entidades.SgExemplar;
import entidades.Users;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Nobrega
 */
public class Circulacao1Controller extends SelectorComposer<Component> {

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();
    DecimalFormat df = new DecimalFormat("#.00");
    
    @Wire
    Listbox emprestimoLista;
    
    @Wire
    Listheader colunaDev, colunaOperacao1, colunaOperacao2, colunaMulta;
    
    @Wire
    Button btnRenovarEmp, btnDevolverEmp;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());

        pesquisarEmprestimosAbertos();
    }

    public void pesquisarEmprestimosAbertos() {
        ListModelList<SgEmprestimo> emprestimos = new ListModelList<SgEmprestimo>(new SgEmprestimoJpaController(new JPA().getEmf()).findSgEmprestimoEntities());
        ListModelList<SgEmprestimo> subEmprestimos = new ListModelList<SgEmprestimo>();
        for (SgEmprestimo emprestimo : emprestimos) {
            if (emprestimo.getEstado() != null && emprestimo.getEstado().equals("Activo")) {
                subEmprestimos.add(emprestimo);
            }
        }
        emprestimoLista.setModel(subEmprestimos);
    }

    //Evento para devolucao da obra
    @Listen("onDevolverObra =#emprestimoLista")
    public void subtrairLista2(ForwardEvent evt) {
        Image img = (Image) evt.getOrigin().getTarget();

        Listitem litem = (Listitem) img.getParent().getParent();

        SgEmprestimo empre = litem.getValue();
        devolverObra(empre);
        pesquisarEmprestimosAbertos();
    }

    //Funcao para devolver uma obra
    public void devolverObra(SgEmprestimo e) {
        e.setDataDevolucao(new Date());
        e.setEstado("Devolvido");
        SgExemplar exe = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(e.getExemplarRef().getNrRegisto());
        exe.setEstado("Disponivel");
        try {
            new SgEmprestimoJpaController(new JPA().getEmf()).edit(e);
            new SgExemplarJpaController(new JPA().getEmf()).edit(exe);
        } catch (Exception ex) {
            Clients.showNotification("Não foi possivel registar a devolução", "warning", null, null, 3000);
        }
        devolucaoAtrasada(e);
    }

    //Funcao para verificar se a devolucao esta em dia
    public void devolucaoAtrasada(SgEmprestimo e) {
        if (isLeituraInterna(e)) {
            int dia = e.getDataEmprestimo().getDate();
            int mes = e.getDataEmprestimo().getMonth();
            int ano = e.getDataEmprestimo().getYear();
            Date dataEmp = new Date(ano, mes, dia);
            dia = new Date().getDate();
            mes = new Date().getMonth();
            ano = new Date().getYear();
            Date actual = new Date(ano, mes, dia);
            if (dataEmp.before(actual)) {
                int days = daysBetween(dataEmp, actual);
                double valor = days*50;
                e.setMultaCriacaodata(new Date());
                e.setMultaEstado("Nao paga");
                e.setMultaValor(String.valueOf(valor));
                try {
                    new SgEmprestimoJpaController(new JPA().getEmf()).edit(e);
                    Clients.showNotification("Multa registada no valor de: "+df.format(valor)+" Meticais", "warning", null, null, 5000);
                } catch (Exception ex) {
                    Clients.showNotification("Nao foi possivel registar a multa", "warning", null, null, 3000);
                }
                
            }
        }
    }
    
    //Funcao para calcular o intervalo em dias existente entre duas datas
    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    //Funcao para verificar se a leitura eh interna
    public Boolean isLeituraInterna(SgEmprestimo e) {
        return e.getTipoEmprestimo() != null && e.getTipoEmprestimo().equals("Interna");
    }

    //Evento para fazer o reload da lista de emprestimos
    @Listen("onClick = #reloadEmprestimo")
    public void reloadListaEmprestimo() {
        colunaDev.setVisible(false);
        colunaMulta.setVisible(false);
        colunaOperacao1.setVisible(true);
        colunaOperacao2.setVisible(false);
        pesquisarEmprestimosAbertos();
    }
    
    @Listen("onClick = #menuDev")
    public void mostrarListaDevolucoes(){
        colunaDev.setVisible(true);
        colunaMulta.setVisible(false);
        colunaOperacao1.setVisible(false);
        colunaOperacao2.setVisible(false);
        pesquisarEmprestimosDevolvidos();
        
    }
    
    @Listen("onClick = #menuMul")
    public void mostrarListaMultas(){
        colunaDev.setVisible(true);
        colunaOperacao1.setVisible(false);
        colunaOperacao2.setVisible(true);
        colunaMulta.setVisible(true);
        pesquisarMultasNaoPagas();
    }
    
    public void pesquisarEmprestimosDevolvidos(){
        ListModelList<SgEmprestimo> emprestimos = new ListModelList<SgEmprestimo>(new SgEmprestimoJpaController(new JPA().getEmf()).findSgEmprestimoEntities());
        ListModelList<SgEmprestimo> subEmprestimos = new ListModelList<SgEmprestimo>();
        for (SgEmprestimo emprestimo : emprestimos) {
            if (emprestimo.getEstado() != null && emprestimo.getEstado().equals("Devolvido")) {
                subEmprestimos.add(emprestimo);
            }
        }
        emprestimoLista.setModel(subEmprestimos);
    }
    
    public void pesquisarMultasNaoPagas(){
        ListModelList<SgEmprestimo> emprestimos = new ListModelList<SgEmprestimo>(new SgEmprestimoJpaController(new JPA().getEmf()).findSgEmprestimoEntities());
        ListModelList<SgEmprestimo> subEmprestimos = new ListModelList<SgEmprestimo>();
        for (SgEmprestimo emprestimo : emprestimos) {
            if (emprestimo.getEstado() != null && emprestimo.getEstado().equals("Devolvido")) {
                if(emprestimo.getMultaEstado() != null && emprestimo.getMultaEstado().equals("Nao paga")){
                    subEmprestimos.add(emprestimo);
                }
            }
        }
        emprestimoLista.setModel(subEmprestimos);
    }
    
    public void renovarEmprestimo(SgEmprestimo e){
        
    }
    
    public Boolean isRenovado(SgEmprestimo e){
        if(e.getEstadoRenovacao()!= null)
            return true;
        return false;
    }
}
