/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BLeitorJpaController;
import controladores.entidades.SgEmprestimoJpaController;
import controladores.entidades.SgExemplarJpaController;
import controladores.entidades.SgObraJpaController;
import controladores.entidades.UsersJpaController;
import controladores.entidades.exceptions.NonexistentEntityException;
import entidades.BLeitor;
import entidades.SgEmprestimo;
import entidades.SgExemplar;
import entidades.SgObra;
import entidades.Users;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Nobrega
 */
public class CirculacaoController extends SelectorComposer<Component> {

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();
    BLeitor leitor;
    SgObra Obra;
    SgEmprestimo emprestimo;
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyy", Locale.ENGLISH);

    @Wire
    Panel painelF, painelV, painelE, painelLivro;

    @Wire
    Textbox utente_search;

    @Wire
    Listbox livros;

    @Wire
    Radio interna, domiciliar;

    @Wire
    Label id_leitorV, nome_leitorV, bi_leitorV, moradia_leitorV, tel_leitorV, tipo_leitor, estado_leitor,
            id_leitorF, nome_leitorF, nr_funcionario, telefone_leitorF, categoria_leitorF, faculdade_leitorF,
            estado_leitorF, id_leitorE, nome_leitorE, nr_estudante, ano_ingresso, categoria_leitorE, curso_leitorE,
            estado_leitorE;

    @Wire
    Label tituloLivro, volumeLivro, edicaoLivro, cotaLivro, isbnLivro, anoLivro, localEdicaoLivro, editoraLivro,
            categoriaLivro, areaLivro, tipoLivro, idiomaLivro, formatoLivro, exemplaresLivro, dataRegistoLivro,
            nomeJornal, nomeLabel, isbnLabel, edicaoLabel, volumeLabel, editoraLabel;

    @Wire
    Decimalbox nrExemplar;

    @Wire
    Button editNrExemplar;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
    }

    @Listen("onChanging=#utente_search")
    public void doUtenteSearch(InputEvent event) {
        String frase = event.getValue();
        if (Strings.isBlank(frase)) {
            painelE.setVisible(false);
            painelV.setVisible(false);
            painelF.setVisible(false);
        } else {

            BLeitor lei = new BLeitorJpaController(new JPA().getEmf()).findBLeitor(Long.parseLong(frase));
            if (lei == null) {
                Clients.showNotification("Utente não encontrado!", "warning", null, null, 3000);
            } else {
                leitor = lei;
                if (leitor.getTipoLeitor().equals("Visitante")) {
                    preencherV(leitor);
                } else if (leitor.getTipoLeitor().equals("Funcionario")) {
                    preencherF(leitor);
                } else {
                    preencherE(leitor);
                }
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
        alterarEstadoV(leitor, estado_leitor);
        painelV.setVisible(true);
        painelE.setVisible(false);
        painelF.setVisible(false);
    }

    public void alterarEstadoV(BLeitor l, Label estadoL) {
        if (l.getEstado().equals("Activo")) {
            estadoL.setValue(l.getEstado());
            estadoL.setSclass("label label-success");
        } else {
            estadoL.setValue(l.getEstado());
            estadoL.setSclass("label label-danger");
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
        alterarEstadoV(leitorF, estado_leitorF);
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
        alterarEstadoV(leitorE, estado_leitorE);
        painelF.setVisible(false);
        painelE.setVisible(true);
        painelV.setVisible(false);
    }

    //========================================================================================================
    //PESQUISAR LIVROS
    //========================================================================================================
    public void pesquisarLivros(String titulo) {
        ListModelList<SgObra> books = new ListModelList<SgObra>(new SgObraJpaController(new JPA().getEmf()).findSgObraEntities());
        ListModelList<SgObra> subBooks = new ListModelList<SgObra>();
        for (SgObra book : books) {
            if (book.getTitulo().toUpperCase().contains(titulo)) {
                subBooks.add(book);
            }
        }
        livros.setModel(subBooks);
    }

    @Listen("onChanging = #titlesearch")
    public void doPesquisarBook(InputEvent event) {
        String frase = event.getValue().toUpperCase();
        painelLivro.setVisible(false);
        nrExemplar.setDisabled(true);
        if (Strings.isBlank(frase)) {
            livros.setVisible(false);
        } else {
            livros.setVisible(true);
            pesquisarLivros(frase);
        }
    }

    @Listen("onSelect = #livros")
    public void mostrarObra() {
        Obra = livros.getSelectedItem().getValue();
        livros.setVisible(false);
        painelLivro.setVisible(true);
        mostrarLivro(Obra);
        if (exemplarDispo(Obra) != null) {
            SgExemplar exe = exemplarDispo(Obra);
            nrExemplar.setValue(exe.getNrRegisto().toString());
        }
    }

    public void mostrarLivro(SgObra O) {
        tituloLivro.setValue(O.getTitulo());
        cotaLivro.setValue(O.getCota());
        anoLivro.setValue(O.getPublicacaoAno().toString());
        tipoLivro.setValue(O.getTipoObra());
        areaLivro.setValue(O.getArea().getDescricao());
        idiomaLivro.setValue(O.getIdioma());
        categoriaLivro.setValue(O.getDominio().getDescricao());
        dataRegistoLivro.setValue(formatter.format(O.getCadastroData()));
        if (qtdExemplaresDispo(O) != 0) {
            String qtd = "" + qtdExemplaresDispo(O);
            exemplaresLivro.setValue(qtd);
            exemplaresLivro.setSclass("label label-success");
        } else {
            exemplaresLivro.setValue("Nenhum");
            exemplaresLivro.setSclass("label label-danger");
        }
        if (O.getTipoObra().equals("Livro")) {
            nomeLabel.setVisible(false);
            isbnLabel.setVisible(true);
            volumeLabel.setVisible(true);
            edicaoLabel.setVisible(true);
            editoraLabel.setVisible(true);
            nomeJornal.setVisible(false);
            volumeLivro.setValue(O.getVolume().toString());
            edicaoLivro.setValue(O.getEdicao().toString());
            isbnLivro.setValue(O.getIsbn());
            editoraLivro.setValue(O.getEditora());
            localEdicaoLivro.setValue(O.getEdicaoCidade());
        }
        if (O.getTipoObra().equals("Jornal") || O.getTipoObra().equals("Revista")) {
            nomeLabel.setVisible(true);
            nomeJornal.setVisible(true);
            isbnLabel.setVisible(false);
            volumeLabel.setVisible(true);
            edicaoLabel.setVisible(true);
            volumeLivro.setVisible(true);
            edicaoLivro.setVisible(true);
            editoraLivro.setVisible(true);
            editoraLabel.setVisible(true);
            nomeJornal.setValue(O.getNome());
            volumeLivro.setValue(O.getVolume().toString());
            edicaoLivro.setValue(O.getEdicao().toString());
            isbnLivro.setValue(O.getIsbn());
            editoraLivro.setValue(O.getEditora());
            localEdicaoLivro.setValue(O.getEdicaoCidade());
        }
        if (O.getTipoObra().equals("Monografia")) {
            nomeLabel.setVisible(false);
            nomeJornal.setVisible(false);
            volumeLivro.setVisible(false);
            isbnLabel.setVisible(false);
            volumeLabel.setVisible(false);
            edicaoLabel.setVisible(false);
            edicaoLivro.setVisible(false);
            editoraLabel.setVisible(false);
            editoraLivro.setVisible(false);
            localEdicaoLivro.setValue(O.getEdicaoCidade());

        }
    }

    public int qtdExemplares(SgObra O) {
        int qtd = 0;
        ListModelList<SgExemplar> ex = new ListModelList<SgExemplar>(new SgExemplarJpaController(new JPA().getEmf()).findSgExemplarEntities());
        for (SgExemplar exemplar : ex) {
            if (exemplar.getObraRef().equals(O)) {
                qtd++;
            }
        }
        return qtd;
    }

    public int qtdExemplaresDispo(SgObra O) {
        int qtd = 0;
        ListModelList<SgExemplar> ex = new ListModelList<SgExemplar>(new SgExemplarJpaController(new JPA().getEmf()).findSgExemplarEntities());
        for (SgExemplar exemplar : ex) {
            if (exemplar.getObraRef().equals(O)) {
                if (exemplar.getEstado().equals("Disponivel")) {
                    qtd++;
                }
            }
        }
        return qtd;
    }

    @Listen("onClick = #emprestar")
    public void emprestarObra() {
        SgExemplar exempl = exemplarDispo(Obra);
        if (!painelE.isVisible() && !painelF.isVisible() && !painelV.isVisible() && !painelLivro.isVisible()) {
            Clients.showNotification("Escolha primeiro um utente e uma obra", "warning", null, null, 3000);
        } else {
            if (!painelE.isVisible() && !painelF.isVisible() && !painelV.isVisible()) {
                Clients.showNotification("Não escolheu nenhum utente", "warning", null, null, 3000);
                return;
            }
            if (!painelLivro.isVisible()) {
                Clients.showNotification("Não escolheu nenhuma obra", "warning", null, null, 3000);
                return;
            }
            if (leitor.getEstado().equals("Inactivo")) {
                Clients.showNotification("O utente escolhido não pode emprestar nenhuma obra", "warning", null, null, 4000);
                return;
            }
            if (exemplarDispo(Obra) == null) {
                Clients.showNotification("Não tem exemplares disponiveis para a obra escolhida", "warning", null, null, 4000);
                return;
            }
            if (leitor.getTipoLeitor().equals("Visitante") && domiciliar.isSelected()) {
                Clients.showNotification("Um visitante não pode emprestar obras para leitura domiciliar", "warning", null, null, 4000);
                return;
            }
            if ((Obra.getTipoObra().equals("Revista") || Obra.getTipoObra().equals("Jornal")
                    || Obra.getTipoObra().equals("Monografia")) && domiciliar.isSelected()) {
                Clients.showNotification("Periódicos e monografias não podem se emprestar para leitura domiciliar", "warning", null, null, 4000);
                return;
            }
            if (qtdEmpAbertos(leitor) >= 3) {
                Clients.showNotification("O utente atingiu o numero maximo de emprestimos", "warning", null, null, 4000);
                return;
            }
            if (mesmaObra(leitor, Obra)) {
                Clients.showNotification("O utente tem em sua posse um exemplar da mesma obra", "warning", null, null, 4000);
                return;
            }
            String tipoEmp;
            if (interna.isSelected()) {
                tipoEmp = "Interna";
            } else {
                tipoEmp = "Domiciliar";
            }
            if (!nrExemplar.isDisabled()) {
                if (!exemplarISselectedObra(Long.parseLong(nrExemplar.getValue().toString()), Obra)) {
                    Clients.showNotification("Informe um exemplar que seja da mesma obra que seleccionou", "warning", null, null, 4000);
                    return;
                } else {
                    exempl = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(Long.parseLong(nrExemplar.getValue().toString()));
                    if (exempl.getEstado().equals("Emprestado") || exempl.getEstado().equals("Fixo")) {
                        Clients.showNotification("O exemplar que escolheu encontra-se indisponivel", "warning", null, null, 4000);
                        return;
                    }
                }
            }

            SgEmprestimo emp = new SgEmprestimo();
            emp.setIdemprestimo(Long.MIN_VALUE);
            emp.setAgenteBibliot(currentUser);
            emp.setDataEmprestimo(new Date());
            emp.setEstado("Activo");
            emp.setExemplarRef(exempl);
            emp.setIdLeitor(leitor);
            emp.setTipoEmprestimo(tipoEmp);

            try {
                new SgEmprestimoJpaController(new JPA().getEmf()).create(emp);
                SgExemplar exemp = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(exempl.getNrRegisto());
                exemp.setEstado("Emprestado");
                new SgExemplarJpaController(new JPA().getEmf()).edit(exemp);
                mostrarObra();
                nrExemplar.setDisabled(true);
                nrExemplar.setValue(exemplarDispo(Obra).getNrRegisto().toString());
                Clients.showNotification("Emprestimo realizado com sucesso", "info", null, null, 3000);
            } catch (NonexistentEntityException ex) {
                Clients.showNotification("O exemplar escolhido não existe", "warning", null, null, 3000);
            } catch (Exception ex) {
                Clients.showNotification("Não foi possivel registar o emprestimo", "warning", null, null, 3000);
            }
        }
    }

    public SgExemplar exemplarDispo(SgObra o) {
        SgExemplar exemp = null;
        SgObra book = new SgObraJpaController(new JPA().getEmf()).findSgObra(o.getIdlivro());
        List<SgExemplar> listaExemp = book.getSgExemplarList();
        for (SgExemplar sgExemplar : listaExemp) {
            if (sgExemplar.getEstado().equals("Disponivel")) {
                return sgExemplar;
            }
        }
        return exemp;
    }

    public int qtdEmpAbertos(BLeitor leit) {
        BLeitor reader = new BLeitorJpaController(new JPA().getEmf()).findBLeitor(leit.getNrCartao());
        List<SgEmprestimo> listEmp = reader.getSgEmprestimoList();
        int qtdEmp = 0;
        for (SgEmprestimo sgEmprestimo : listEmp) {
            if (sgEmprestimo.getEstado() != null && sgEmprestimo.getEstado().equals("Activo")) {
                qtdEmp++;
            }
        }
        return qtdEmp;
    }

    //funcao para verificar se o leitor tem em sua posse um exemplar da obra que deseja emprestar
    public Boolean mesmaObra(BLeitor leit, SgObra book) {
        BLeitor reader = new BLeitorJpaController(new JPA().getEmf()).findBLeitor(leit.getNrCartao());
        List<SgEmprestimo> listEmp = reader.getSgEmprestimoList();
        for (SgEmprestimo sgEmprestimo : listEmp) {
            if (sgEmprestimo.getEstado() != null && sgEmprestimo.getEstado().equals("Activo")
                    && sgEmprestimo.getExemplarRef().getObraRef().equals(book)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isOcupado(SgExemplar exe) {
        if (!exe.getEstado().equals("Disponivel")) {
            return true;
        }
        return false;
    }

    //Funcao para verificar se o numero de registo dado eh um realmente um exemplar
    public Boolean isExemplar(Long nrRegisto) {
        SgExemplar ex = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(nrRegisto);
        return ex != null;
    }

    //Funcao para verificar se o exemplar dado eh da obra seleccionada
    public Boolean exemplarISselectedObra(Long nrRegisto, SgObra book) {
        if (isExemplar(nrRegisto)) {
            SgExemplar ex = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(nrRegisto);
            if (ex.getObraRef().equals(book)) {
                return true;
            }
        }
        return false;
    }
}
