/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.EstudanteJpaController;
import controladores.entidades.FuncionarioJpaController;
import controladores.entidades.BLeitorJpaController;
import controladores.entidades.BNotificacaoJpaController;
import controladores.entidades.SgEmprestimoParametrosJpaController;
import controladores.entidades.UsersJpaController;
import entidades.Estudante;
import entidades.Funcionario;
import entidades.BLeitor;
import entidades.BNotificacao;
import entidades.Users;
import java.util.Date;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;
import email.Email;
import entidades.SgEmprestimoParametros;
import org.apache.commons.mail.EmailException;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Radio;

/**
 *
 * @author Nobrega
 */
public class UtenteController extends SelectorComposer<Component> {

    @Wire
    Textbox nome_visitante, moradia_visitante, email_visitante, telefone_visitante,
            bi_visitante, text_pesquisa, nomeEV, moradiaEV, emailEV,
            telefoneEV, motivoB;
    @Wire
    Decimalbox utente_search;

    @Wire
    Label id_leitorV, nome_leitorV, bi_leitorV, moradia_leitorV, email_leitorV, tel_leitorV,
            tipo_leitor, estado_leitor, id_leitorF, nome_leitorF, nr_funcionario, telefone_leitorF,
            categoria_leitorF, estado_leitorF, faculdade_leitorF, id_leitorE, nome_leitorE, nr_estudante,
            ano_ingresso, categoria_leitorE, estado_leitorE, curso_leitorE;

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();

    @Wire
    Listbox utente_ident, funcionarioListBox, estudanteListBox, listEstudantes, listFuncionarios,
            listVisitante, listEstudante, listFuncionario;
    @Wire
    Messagebox Sim, Cancelar;

    @Wire
    Window win2;

    @Wire
    Radiogroup rd;

    @Wire
    Radio estudante, funcionario;

    @Wire
    Button bloquearV, desbloquearV, editarV, bloquearF, desbloquearF, bloquearE, desbloquearE,
            detalhesV, detalhesE, detalhesF, imprimirV, imprimirE, imprimirF, btn_utente_search,
            gerarC;
    @Wire
    Separator espaco;

    @Wire
    Panel motivoBlock;

    @Wire
    Menuitem search_funcionario, search_estudante, search_utente;

    @Wire
    Menupopup m2;

    @Wire
    Div painelV, editVisitante, painel_edicao, painelF, painelE, cce, ccf, listagem, div_pesquisar;

    ListModelList<Users> utilizadores;
    ListModelList<Users> us = new ListModelList<Users>();
    ListModelList<BLeitor> leitores = new ListModelList<BLeitor>();

    Estudante e;
    Funcionario f;
    BLeitor leitor;
    String selectedSearch;

    //Email email = new SimpleEmail();
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
    }

    @Listen("onClick=#save_visitante; onOk=#utente_search")
    public void doCadastroVisitante() {

        if (Strings.isBlank(nome_visitante.getValue()) || Strings.isBlank(moradia_visitante.getValue())
                || Strings.isBlank(email_visitante.getValue()) || Strings.isBlank(telefone_visitante.getValue())
                || Strings.isBlank(bi_visitante.getValue())) {
            Clients.showNotification("Preencha todos os campos", "warning", null, null, 3000);
        } else if (verificarBI(bi_visitante.getValue()) == true) {
            Clients.showNotification("O utente já existe!", "warning", null, null, 3000);
        } else {
            BLeitor l = new BLeitor();
            l.setNrCartao(Long.MIN_VALUE);
            l.setNome(nome_visitante.getValue());
            l.setMoradia(moradia_visitante.getValue());
            l.setEmail(email_visitante.getValue());
            l.setTelefone(telefone_visitante.getValue());
            l.setBi(bi_visitante.getValue());
            l.setTipoLeitor("Visitante");
            l.setEstado("Activo");
            l.setIdagente(currentUser);
            l.setDataRegisto(new Date());
            long param = 1;
            SgEmprestimoParametros novoCartao = new SgEmprestimoParametrosJpaController(new JPA().getEmf()).findSgEmprestimoParametros(param);
            l.setIdParametroRegisto(novoCartao);
            try {
                new BLeitorJpaController(new JPA().getEmf()).create(l);
                Clients.showNotification("Utente registado com sucesso! Seu identificador é: " + l.getNrCartao(), "info", null, null, 5000);
                nome_visitante.setValue(null);
                moradia_visitante.setValue(null);
                email_visitante.setValue(null);
                telefone_visitante.setValue(null);
                bi_visitante.setValue(null);
            } catch (Exception ex) {
                Clients.showNotification("Nao foi possivel registar o utente!", "warning", null, null, 3000);
            }
        }

    }

    @Listen("onChanging=#utente_search")
    public void doUtenteSearch(InputEvent event) {
        String frase = event.getValue();
        editVisitante.setVisible(false);
        motivoBlock.setVisible(false);
        listagem.setVisible(false);
        div_pesquisar.setVisible(false);

        if (Strings.isBlank(frase)) {
            if (editVisitante.isVisible()) {
                editVisitante.setVisible(false);
            }
        } else {

            BLeitor lei = new BLeitorJpaController(new JPA().getEmf()).findBLeitor(Long.parseLong(frase));
            if (lei == null) {
                if (editVisitante.isVisible()) {
                    editVisitante.setVisible(false);
                } else if (utente_ident.isVisible()) {
                    utente_ident.setVisible(false);
                }
                editVisitante.setVisible(false);
                Clients.showNotification("Utente não encontrado!", "warning", utente_search, "after_start", 3000);
            } else {
                leitor = lei;
                if (leitor.getTipoLeitor().equals("Visitante")) {
                    editVisitante.setVisible(true);
                    painelV.setVisible(true);
                    preencherV(leitor);
                } else if (leitor.getTipoLeitor().equals("Funcionario")) {
                    editVisitante.setVisible(true);
                    painelF.setVisible(true);
                    preencherF(leitor);
                } else {
                    editVisitante.setVisible(true);
                    painelE.setVisible(true);
                    preencherE(leitor);
                }
            }
        }

    }

    @Listen("onChanging = #text_pesquisa")
    public void doUtenteCadastroSearch(InputEvent event) {
        String frase = event.getValue().toUpperCase();
        //Verificando se foi preenchido algo no textfield, se nao mostra-se a lista
        espaco.setVisible(false);
        cce.setVisible(false);
        ccf.setVisible(false);
        gerarC.setDisabled(true);
        funcionarioListBox.setVisible(false);
        estudanteListBox.setVisible(false);
        if (Strings.isBlank(frase)) {
            gerarC.setDisabled(true);
            espaco.setVisible(true);
            funcionarioListBox.setVisible(false);
            estudanteListBox.setVisible(false);

        } else if (estudante.isSelected()) {
            text_pesquisa.setPlaceholder("Pesquisa por nome do estudante");
            estudanteListBox.setVisible(true);
            pesquisarEstudante(frase);
        } else {
            text_pesquisa.setPlaceholder("Pesquisa por nome do funcionario");
            funcionarioListBox.setVisible(true);
            pesquisarFuncionario(frase);
        }
    }

    @Listen("onClick=#listar_utente")
    public void doListarVisitantes() {
        div_pesquisar.setVisible(false);
        listEstudantes.setVisible(false);
        listFuncionarios.setVisible(false);
        editVisitante.setVisible(false);
        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> ll = new ListModelList<BLeitor>();
        for (BLeitor l : leitores) {
            if (l.getTipoLeitor().equals("Visitante")) {
                ll.add(l);
            }
        }
        listagem.setVisible(true);
        utente_ident.setVisible(true);
        utente_ident.setModel(ll);
    }

    @Listen("onClick = #listar_estudantes")
    public void doListarUtenteEstudante() {
        div_pesquisar.setVisible(false);
        editVisitante.setVisible(false);
        utente_ident.setVisible(false);
        listFuncionarios.setVisible(false);

        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> ll = new ListModelList<BLeitor>();
        for (BLeitor lei : leitores) {
            if (lei.getTipoLeitor().equals("Estudante")) {
                ll.add(lei);
            }
        }
        listagem.setVisible(true);
        listEstudantes.setModel(ll);
        listEstudantes.setVisible(true);
    }

    @Listen("onClick = #listar_funcionarios")
    public void doListarFuncionario() {
        div_pesquisar.setVisible(false);
        editVisitante.setVisible(false);
        utente_ident.setVisible(false);
        listEstudantes.setVisible(false);

        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> ll = new ListModelList<BLeitor>();
        for (BLeitor lei : leitores) {
            if (lei.getTipoLeitor().equals("Funcionario")) {
                ll.add(lei);
            }
        }
        listagem.setVisible(true);
        listFuncionarios.setModel(ll);
        listFuncionarios.setVisible(true);
    }

    @Listen("onClick=#editarV")
    public void doPreencherLeitorV() {

        painel_edicao.setVisible(true);
        detalhesV.setDisabled(true);
        imprimirV.setDisabled(true);
        editarV.setDisabled(true);
        bloquearV.setDisabled(true);
        desbloquearV.setDisabled(true);
        nomeEV.setValue(leitor.getNome());
        moradiaEV.setValue(leitor.getMoradia());
        emailEV.setValue(leitor.getEmail());
        telefoneEV.setValue(leitor.getTelefone());
    }

    @Listen("onClick = #editV")
    public void doEditarLeitorV() {
        if (Strings.isBlank(nomeEV.getValue()) || Strings.isBlank(moradiaEV.getValue()) || Strings.isBlank(telefoneEV.getValue())
                || Strings.isBlank(emailEV.getValue())) {
            Clients.showNotification("Preencha todos os campos", "warning", editVisitante, "before_center", 3000);
        } else {
            leitor.setNome(nomeEV.getValue());
            leitor.setMoradia(moradiaEV.getValue());
            leitor.setEmail(emailEV.getValue());
            leitor.setTelefone(telefoneEV.getValue());
            try {
                new BLeitorJpaController(new JPA().getEmf()).edit(leitor);
                Clients.showNotification("Utente editado com sucesso!", "info", null, null, 3000);
                preencherV(leitor);
            } catch (Exception ex) {
                Clients.showNotification("Não foi possível editar o utente!", "warning", null, null, 3000);
            }
        }
    }

    @Listen("onClick = #desbloquearV; onClick = #desbloquearE; onClick = #desbloquearF")
    public void doDesbloqueioV() {
        if (leitor.getEstado().equals("Inactivo")) {
            leitor.setEstado("Activo");
            Email emaill = new Email();
            try {
                new BLeitorJpaController(new JPA().getEmf()).edit(leitor);
                Clients.showNotification("Utente desbloqueado com sucesso!", "info", null, null, 3000);
            } catch (Exception ex) {
                Clients.showNotification("Não foi possível desbloquear o utente!", "warning", null, null, 3000);
            }
            if (leitor.getTipoLeitor().equals("Visitante")) {
                try {
                    emaill.sendUnblockEmail(leitor.getEmail());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitor, desbloquearV, bloquearV);
            }
            if (leitor.getTipoLeitor().equals("Estudante")) {
                try {
                    emaill.sendUnblockEmail(leitor.getIdutilizador().getEmail());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitorE, desbloquearE, bloquearE);
            }
            if (leitor.getTipoLeitor().equals("Funcionario")) {
                try {
                    emaill.sendUnblockEmail(leitor.getIdutilizador().getEmail());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitorF, desbloquearF, bloquearF);
            }
        }
    }

    @Listen("onClick = #aceitarB;")
    public void doBloqueioV() {
        if (Strings.isBlank(motivoB.getValue())) {
            Clients.showNotification("Preencha o campo!", "warning", motivoB, "after_center", 3000);
        } else if (leitor.getEstado().equals("Activo")) {
            leitor.setEstado("Inactivo");
            Email emai = new Email();
            BNotificacao notif = new BNotificacao();
            notif.setIdNotificacao(Long.MIN_VALUE);
            notif.setIdLeitor(leitor);
            notif.setEmissor(currentUser.getUtilizador());
            notif.setDataEnvio(new Date());
            notif.setMensagem(motivoB.getValue());
            try {
                new BLeitorJpaController(new JPA().getEmf()).edit(leitor);
                new BNotificacaoJpaController(new JPA().getEmf()).create(notif);
                motivoBlock.setVisible(false);
                Clients.showNotification("Utente bloqueado com sucesso!", "info", null, null, 3000);
            } catch (Exception ex) {
                Clients.showNotification("Não foi possível bloquear o utente!", "warning", null, null, 3000);
            }
            if (leitor.getTipoLeitor().equals("Visitante")) {
                try {
                    emai.sendBlockEmail(leitor.getEmail(), notif.getMensagem());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitor, desbloquearV, bloquearV);
                editarV.setDisabled(false);
                detalhesV.setDisabled(false);
                imprimirV.setDisabled(false);
            }
            if (leitor.getTipoLeitor().equals("Estudante")) {
                try {
                    emai.sendBlockEmail(leitor.getIdutilizador().getEmail(), notif.getMensagem());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitorE, desbloquearE, bloquearE);
                detalhesE.setDisabled(false);
                imprimirE.setDisabled(false);
            }
            if (leitor.getTipoLeitor().equals("Funcionario")) {
                try {
                    emai.sendBlockEmail(leitor.getIdutilizador().getEmail(), notif.getMensagem());
                } catch (EmailException ex) {
                    Clients.showNotification("O envio do email falhou", "warning", null, null, 3000);
                }
                alterarEstadoV(leitor, estado_leitorF, desbloquearF, bloquearF);
                detalhesF.setDisabled(false);
                imprimirF.setDisabled(false);
            }
        }
    }

    public void preencherV(BLeitor leitorV) {
        id_leitorV.setValue(leitorV.getNrCartao().toString());
        id_leitorV.setSclass("label-id-leitor");
        nome_leitorV.setValue(leitorV.getNome().toUpperCase());
        nome_leitorV.setSclass("label-nome");
        bi_leitorV.setValue(leitorV.getBi());
        bi_leitorV.setSclass("label-nome");
        moradia_leitorV.setValue(leitorV.getMoradia().toUpperCase());
        moradia_leitorV.setSclass("label-nome");
        tel_leitorV.setValue(leitorV.getTelefone());
        tel_leitorV.setSclass("label-nome");
        tipo_leitor.setValue(leitorV.getTipoLeitor().toUpperCase());
        tipo_leitor.setSclass("label-nome");
        alterarEstadoV(leitor, estado_leitor, desbloquearV, bloquearV);
        editarV.setDisabled(false);
        utente_ident.setVisible(false);
        painelV.setVisible(true);
        painelE.setVisible(false);
        painelF.setVisible(false);
    }

    public void alterarEstadoV(BLeitor l, Label estadoL, Button desbloquear, Button bloquear) {
        if (l.getEstado().equals("Activo")) {
            estadoL.setValue(l.getEstado());
            estadoL.setSclass("label label-success");
            desbloquear.setDisabled(true);
            bloquear.setDisabled(false);
        } else {
            estadoL.setValue(l.getEstado());
            estadoL.setSclass("label label-danger");
            bloquear.setDisabled(true);
            desbloquear.setDisabled(false);
        }
    }

    public void preencherF(BLeitor leitorF) {
        id_leitorF.setValue(leitorF.getNrCartao().toString());
        id_leitorF.setSclass("label-id-leitor");
        nome_leitorF.setValue(leitorF.getIdutilizador().getIdFuncionario().getNome().toUpperCase());
        nome_leitorF.setSclass("label-nome");
        nr_funcionario.setValue(leitorF.getIdutilizador().getIdFuncionario().getNrfuncionario());
        nr_funcionario.setSclass("label-nome");
        telefone_leitorF.setValue(leitorF.getIdutilizador().getIdFuncionario().getContacto());
        telefone_leitorF.setSclass("label-nome");
        categoria_leitorF.setValue(leitorF.getTipoLeitor().toUpperCase());
        categoria_leitorF.setSclass("label-nome");
        faculdade_leitorF.setValue(leitorF.getIdutilizador().getIdFuncionario().getFaculdade().getAbreviatura());
        faculdade_leitorF.setSclass("label-nome");
        alterarEstadoV(leitorF, estado_leitorF, desbloquearF, bloquearF);
        utente_ident.setVisible(false);
        painelF.setVisible(true);
        painelV.setVisible(false);
        painelE.setVisible(false);
    }

    public void preencherE(BLeitor leitorE) {
        id_leitorE.setValue(leitorE.getNrCartao().toString());
        id_leitorE.setSclass("label-id-leitor");
        nome_leitorE.setValue(leitorE.getIdutilizador().getIdEstudante().getNomeCompleto().toUpperCase());
        nome_leitorE.setSclass("label-nome");
        nr_estudante.setValue(leitorE.getIdutilizador().getIdEstudante().getNrEstudante());
        nr_estudante.setSclass("label-nome");
        ano_ingresso.setValue(leitorE.getIdutilizador().getIdEstudante().getAnoIngresso().toString());
        ano_ingresso.setSclass("label-nome");
        categoria_leitorE.setValue(leitorE.getTipoLeitor().toUpperCase());
        categoria_leitorE.setSclass("label-nome");
        curso_leitorE.setValue(leitorE.getIdutilizador().getIdEstudante().getCursocurrente().getAbreviatura());
        curso_leitorE.setSclass("label-nome");
        alterarEstadoV(leitorE, estado_leitorE, desbloquearE, bloquearE);
        utente_ident.setVisible(false);
        painelF.setVisible(false);
        painelE.setVisible(true);
        painelV.setVisible(false);
    }

    public boolean verificarBI(String bi) {
        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        boolean match;
        match = false;
        for (BLeitor l : leitores) {
            if (bi.equals(l.getBi())) {
                match = true;
                return match;
            }
        }
        return match;
    }

    public void pesquisarFuncionario(String frase) {
        ListModelList<Funcionario> funcionari = new ListModelList<Funcionario>(new FuncionarioJpaController(new JPA().getEmf()).findFuncionarioEntities());
        ListModelList<Funcionario> func = new ListModelList<Funcionario>();
        func.clear();
        for (Funcionario fun : funcionari) {
            String t = fun.getNome().toUpperCase();
            if (t.contains(frase)) {
                func.add(fun);
            }
        }
        funcionarioListBox.setModel(func);
    }

    public void pesquisarEstudante(String frase) {
        ListModelList<Estudante> estudant = new ListModelList<Estudante>(new EstudanteJpaController(new JPA().getEmf()).findEstudanteEntities());
        ListModelList<Estudante> est = new ListModelList<Estudante>();
        est.clear();
        for (Estudante es : estudant) {
            String t = es.getNomeCompleto().toUpperCase();
            if (t.contains(frase)) {
                est.add(es);
            }
        }
        estudanteListBox.setModel(est);
    }

    @Listen("onSelect = #estudanteListBox")
    public void mostrarCartaoEstudante() {
        ccf.setVisible(false);
        cce.setVisible(true);
        e = estudanteListBox.getSelectedItem().getValue();

//        if (estudanteISleitor(e) != null) {
//            BLeitor bl = estudanteISleitor(e);
//            id_leitorE.setValue(bl.getNrCartao().toString());
//            id_leitorE.setSclass("label-id-leitor");
//        }
        nome_leitorE.setValue(e.getNomeCompleto().toUpperCase());
        nome_leitorE.setSclass("label-nome");
        nr_estudante.setValue(e.getNrEstudante());
        nr_estudante.setSclass("label-nome");
        ano_ingresso.setValue(e.getAnoIngresso().toString());
        ano_ingresso.setSclass("label-nome");
        categoria_leitorE.setValue("Estudante");
        categoria_leitorE.setSclass("label-nome");
        curso_leitorE.setValue(e.getCursocurrente().getAbreviatura());
        curso_leitorE.setSclass("label-nome");
        gerarC.setDisabled(false);
    }

    @Listen("onSelect = #funcionarioListBox")
    public void mostrarCartaoFuncionario() {
        cce.setVisible(false);
        ccf.setVisible(true);
        f = funcionarioListBox.getSelectedItem().getValue();

//        if (funcionarioISleitor(f) != null) {
//            BLeitor bl = funcionarioISleitor(f);
//            id_leitorF.setValue(bl.getNrCartao().toString());
//            id_leitorF.setSclass("label-id-leitor");
//        }
        nome_leitorF.setValue(f.getNome().toUpperCase());
        nome_leitorF.setSclass("label-nome");
        nr_funcionario.setValue(f.getNrfuncionario());
        nr_funcionario.setSclass("label-nome");
        telefone_leitorF.setValue(f.getContacto());
        telefone_leitorF.setSclass("label-nome");
        categoria_leitorF.setValue("Funcionario");
        categoria_leitorF.setSclass("label-nome");
        faculdade_leitorF.setValue(f.getFaculdade().getAbreviatura());
        faculdade_leitorF.setSclass("label-nome");
        gerarC.setDisabled(false);
    }

    @Listen("onClick = #gerarC")
    public void gerarCartao() {
        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        if (estudanteListBox.isVisible()) {
            if (pegarUser(e) != null) {
                Users usr = pegarUser(e);
                if (userIsLeitor(usr, leitores) == true) {
                    Clients.showNotification("O utente já existe!", "warning", null, null, 3000);
                } else {
                    BLeitor l = new BLeitor();
                    l.setNrCartao(Long.MIN_VALUE);
                    l.setTipoLeitor("Estudante");
                    l.setIdutilizador(usr);
                    l.setEstado("Activo");
                    l.setDataRegisto(new Date());
                    l.setIdagente(currentUser);
                    long param = 1;
                    SgEmprestimoParametros novoCartao = new SgEmprestimoParametrosJpaController(new JPA().getEmf()).findSgEmprestimoParametros(param);
                    l.setIdParametroRegisto(novoCartao);
                    try {
                        new BLeitorJpaController(new JPA().getEmf()).create(l);
                        Clients.showNotification("Utente registado com sucesso!", "info", null, null, 3000);
                        id_leitorE.setValue(l.getNrCartao().toString());
                    } catch (Exception ex) {
                        Clients.showNotification("Nao foi possivel registar o utente!", "warning", null, null, 3000);
                    }
                }
            }
        } else if (funcionarioListBox.isVisible()) {
            if (pegarUser(f) != null) {
                Users usr = pegarUser(f);
                if (userIsLeitor(usr, leitores) == true) {
                    Clients.showNotification("O utente já existe!", "warning", null, null, 3000);
                } else {
                    BLeitor lf = new BLeitor();
                    lf.setNrCartao(Long.MIN_VALUE);
                    lf.setEstado("Activo");
                    lf.setDataRegisto(new Date());
                    long param = 1;
                    lf.setIdagente(currentUser);
                    SgEmprestimoParametros novoCartao = new SgEmprestimoParametrosJpaController(new JPA().getEmf()).findSgEmprestimoParametros(param);
                    lf.setIdParametroRegisto(novoCartao);
                    lf.setTipoLeitor("Funcionario");
                    lf.setIdutilizador(usr);
                    try {
                        new BLeitorJpaController(new JPA().getEmf()).create(lf);
                        Clients.showNotification("Utente registado com sucesso!", "info", null, null, 3000);
                        id_leitorF.setValue(lf.getNrCartao().toString());
                    } catch (Exception ex) {
                        Clients.showNotification("Nao foi possivel registar o utente!", "warning", null, null, 3000);
                    }
                }
            }
        }
    }

    public boolean userIsLeitor(Users u, ListModelList<BLeitor> l) {
        for (BLeitor bl : l) {
            if (bl.getIdutilizador() != null) {
                Users user = bl.getIdutilizador();
                if (user.equals(u)) {
                    return true;
                }
            }
        }
        return false;
    }

    public BLeitor userIsleitor(Users u, ListModelList<BLeitor> l) {
        BLeitor bll = new BLeitor();
        for (BLeitor bl : l) {
            if (bl.getIdutilizador() != null) {
                Users user = bl.getIdutilizador();
                if (user.equals(u)) {
                    return bl;
                }
            }
        }
        return bll;
    }

    public Users pegarUser(Estudante e) {
        ListModelList<Users> u = new ListModelList<Users>(new UsersJpaController(new JPA().getEmf()).findUsersEntities());
        Users user = new Users();
        for (Users users : u) {
            if (users.getIdEstudante() != null) {
                if (users.getIdEstudante().equals(e)) {
                    user = users;
                    return user;
                }
            }
        }
        return user;
    }

    public Users pegarUser(Funcionario f) {
        ListModelList<Users> u = new ListModelList<Users>(new UsersJpaController(new JPA().getEmf()).findUsersEntities());
        Users user = new Users();
        for (Users users : u) {
            if (users.getIdFuncionario() != null) {
                if (users.getIdFuncionario().equals(f)) {
                    user = users;
                    return user;
                }
            }
        }
        return user;
    }

//    public BLeitor estudanteISleitor(Estudante est) {
//        ListModelList<BLeitor> lei = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
//        BLeitor l = new BLeitor();
//        if(pegarUser(est) != null){
//            Users usr = pegarUser(est);
//            if(userIsleitor(usr, lei) != null){
//                l = userIsleitor(usr, lei);
//                return l;
//            }
//            return l;
//        }
//        return l;
//    }
//
//    public BLeitor funcionarioISleitor(Funcionario func) {
//        ListModelList<BLeitor> lei = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
//        BLeitor l = new BLeitor();
//        if(pegarUser(func) != null){
//            Users usr = pegarUser(func);
//            if(userIsleitor(usr, lei) != null)
//                return userIsleitor(usr, lei);
//            else
//                return l;
//        } else
//            return l;
//    }
    @Listen("onChanging = #membersearch")
    public void mostrarListaSelectUtente(InputEvent event) {
        String frase = event.getValue().toUpperCase();
        div_pesquisar.setVisible(true);
        editVisitante.setVisible(false);
        listagem.setVisible(false);

        if (Strings.isBlank(frase)) {
            listVisitante.setVisible(false);
            listEstudante.setVisible(false);
            listFuncionario.setVisible(false);
        } else {
            if (selectedSearch == null || selectedSearch.equals("utente")) {
                listVisitante.setVisible(true);
                listEstudante.setVisible(false);
                listFuncionario.setVisible(false);
                pesquisarUtenteVisitante(frase);
            }
            if (selectedSearch != null && selectedSearch.equals("estudante")) {
                listVisitante.setVisible(false);
                listEstudante.setVisible(true);
                listFuncionario.setVisible(false);
                pesquisarUtenteEstudante(frase);
            }
            if (selectedSearch != null && selectedSearch.equals("funcionario")) {
                listVisitante.setVisible(false);
                listEstudante.setVisible(false);
                listFuncionario.setVisible(true);
                pesquisarUtenteFuncionario(frase);
            }
        }
    }

    @Listen("onClick = #search_utente")
    public void setSelectedSearchU() {
        selectedSearch = "utente";
    }

    @Listen("onClick = #search_estudante")
    public void setSelectedSearchE() {
        selectedSearch = "estudante";
    }

    @Listen("onClick = #search_funcionario")
    public void setSelectedSearchF() {
        selectedSearch = "funcionario";
    }

    public void pesquisarUtenteVisitante(String nome) {
        ListModelList<BLeitor> listaVisitantes = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> sublista = new ListModelList<BLeitor>();
        for (BLeitor member : listaVisitantes) {
            if (member.getNome() != null) {
                if (member.getNome().toUpperCase().contains(nome)) {
                    sublista.add(member);
                }
            }
        }
        listVisitante.setModel(sublista);
    }

    public void pesquisarUtenteEstudante(String frase) {
        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> subLeitores = new ListModelList<BLeitor>();
        for (BLeitor lei : leitores) {
            if (lei.getIdutilizador() != null) {
                if (lei.getIdutilizador().getIdEstudante() != null) {
                    if (lei.getIdutilizador().getIdEstudante().getNomeCompleto().toUpperCase().contains(frase)) {
                        subLeitores.add(lei);
                    }
                }
            }
        }
        listEstudante.setModel(subLeitores);
    }

    public void pesquisarUtenteFuncionario(String frase) {
        leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        ListModelList<BLeitor> subLeitores = new ListModelList<BLeitor>();
        for (BLeitor lei : leitores) {
            if (lei.getIdutilizador() != null) {
                if (lei.getIdutilizador().getIdFuncionario() != null) {
                    if (lei.getIdutilizador().getIdFuncionario().getNome().toUpperCase().contains(frase)) {
                        subLeitores.add(lei);
                    }
                }
            }
        }
        listFuncionario.setModel(subLeitores);
    }

    @Listen("onSelect = #listVisitante; onSelect = #listEstudante; onSelect = #listFuncionario;")
    public void mostrarCartaoLista() {
        div_pesquisar.setVisible(false);
        if (listVisitante.isVisible()) {
            leitor = listVisitante.getSelectedItem().getValue();
        }
        if (listEstudante.isVisible()) {
            leitor = listEstudante.getSelectedItem().getValue();
        }
        if (listFuncionario.isVisible()) {
            leitor = listFuncionario.getSelectedItem().getValue();
        }

        if (leitor.getTipoLeitor().equals("Visitante")) {
            editVisitante.setVisible(true);
            painelV.setVisible(true);
            preencherV(leitor);
        } else if (leitor.getTipoLeitor().equals("Funcionario")) {
            editVisitante.setVisible(true);
            painelF.setVisible(true);
            preencherF(leitor);
        } else {
            editVisitante.setVisible(true);
            painelE.setVisible(true);
            preencherE(leitor);
        }
    }
    
    @Listen("onCheck = #funcionario; onCheck = #estudante")
    public void place(){
        if(estudante.isSelected())
            text_pesquisa.setPlaceholder("Pesquisa por nome do estudante");
        else
            text_pesquisa.setPlaceholder("Pesquisa por nome do funcionario");
    }
}
