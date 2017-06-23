/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.CursoJpaController;
import controladores.entidades.SgAutorJpaController;
import controladores.entidades.SgExemplarJpaController;
import controladores.entidades.SgObraAreaJpaController;
import controladores.entidades.SgObraAutorJpaController;
import controladores.entidades.SgObraCategoriaJpaController;
import controladores.entidades.SgObraJpaController;
import controladores.entidades.UsersJpaController;
import entidades.Curso;
import entidades.SgAutor;
import entidades.SgExemplar;
import entidades.SgObra;
import entidades.SgObraArea;
import entidades.SgObraAutor;
import entidades.SgObraAutorPK;
import entidades.SgObraCategoria;
import entidades.Users;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.zkoss.io.Files;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Nobrega
 */
public class ObraController extends SelectorComposer<Component> {

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();
    private String filePath;
    private boolean fileuploaded = false;
    private Media media;
    SgObra Obra;
    SgExemplar exemp;
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyy", Locale.ENGLISH);

    @Wire
    Listbox combo_curso, combo_categoria, combo_area, lista_autores, lista_autores2, list_tipo,
            comboidioma, comboAquisicao, livros, comboAquis;

    @Wire
    Textbox text_directory, autor_search, exemplares, ano_publicacao, edicao, local_edicao, editora, cota, isbn, volume,
            titulo, nome, qtdExemplares, nrExemplarSearch;

    @Wire
    Button up, guardar, add;

    @Wire
    Checkbox rd_directory;

    @Wire
    Separator sep;

    @Wire
    Div cat, vol, isb, ano_pub, edic, local_edic, edit, extra, cur, div_lista_autores, nom, divPrincipal, divObraAdd,
            divLivro, divEdicao, divExemplar;

    @Wire
    Datebox dataAquisicao, dataAquis;

    @Wire
    Div painelLivro, painelExemplar;

    @Wire
    Label tituloLivro, volumeLivro, edicaoLivro, cotaLivro, isbnLivro, anoLivro, localEdicaoLivro, editoraLivro,
            categoriaLivro, areaLivro, tipoLivro, idiomaLivro, formatoLivro, exemplaresLivro, dataRegistoLivro,
            nomeJornal, nrRegisto, nomeLabelExe, nomeJornalExe, tituloLivroExe, cotaLivroExe, volumeLivroExe,
            edicaoLivroExe, volumeLabelExe, edicaoLabelExe, tipoLivroExe, isbnLabelExe, isbnLivroExe, localEdicaoLivroExe,
            editoraLabelExe, editoraLivroExe, anoLivroExe, idiomaLivroExe, areaLivroExe, categoriaLivroExe, exemplaresLivroExe,
            dataRegistoLivroExe, dataAquisicaoLivroExe, estadoExe;

    @Wire
    Label nomeLabel, isbnLabel, edicaoLabel, volumeLabel, editoraLabel;
    
    @Wire
    Listitem categ;

    //Declaracao da segunda lista
    ListModelList<SgAutor> lista2 = new ListModelList<SgAutor>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());
    }

//    @Listen("onClick = #add")
    public void mostrarDivObraAdd() {
        divPrincipal.setVisible(false);
        divObraAdd.setVisible(true);
    }

    @Listen("onClick = #sairObraAdd")
    public void fecharDivObraAdd() {
        divPrincipal.setVisible(true);
        divObraAdd.setVisible(false);
    }

    @Listen("onCreate = #combo_curso; onCreate = #combo_categoria; onCreate = #combo_area")
    public void doCriarListas() {
        ListModelList<Curso> listaCurso = new ListModelList<Curso>(new CursoJpaController(new JPA().getEmf()).findCursoEntities());
        combo_curso.setModel(listaCurso);
        ListModelList<SgObraCategoria> listaCategoria = new ListModelList<SgObraCategoria>(new SgObraCategoriaJpaController(new JPA().getEmf()).findSgObraCategoriaEntities());
        combo_categoria.setModel(listaCategoria);
        ListModelList<SgObraArea> listaArea = new ListModelList<SgObraArea>(new SgObraAreaJpaController(new JPA().getEmf()).findSgObraAreaEntities());
        combo_area.setModel(listaArea);
    }

    @Listen("onCheck =#rd_directory")
    public void mostrarbtn() {
        if (rd_directory.isChecked()) {
            text_directory.setDisabled(false);
            up.setDisabled(false);
        } else {
            text_directory.setDisabled(true);
            up.setDisabled(true);
        }
    }

    @Listen("onChanging = #autor_search")
    public void doAutorSearch(InputEvent event) {
        String frase = event.getValue().toUpperCase();
        if (Strings.isBlank(frase)) {
            lista_autores.setVisible(false);
            sep.setVisible(true);
        } else {
            sep.setVisible(false);
            lista_autores.setVisible(true);
            pesquisarAutor(frase);
        }
    }

    public void pesquisarAutor(String frase) {
        ListModelList<SgAutor> autores = new ListModelList<SgAutor>(new SgAutorJpaController(new JPA().getEmf()).findSgAutorEntities());
        ListModelList<SgAutor> subAutores = new ListModelList<SgAutor>();
        subAutores.clear();
        for (SgAutor subAutore : autores) {
            if (subAutore.getNome() != null) {
                String autorNome = subAutore.getNome().toUpperCase();
                if (autorNome.contains(frase)) {
                    subAutores.add(subAutore);
                }
            }
        }
        lista_autores.setModel(subAutores);
    }

    @Listen("onSelect = #lista_autores")
    public void preencherLista2() {
        for (SgAutor a : lista2) {
            if (lista_autores.getSelectedItem().getValue().equals(a)) {
                Clients.showNotification("O autor seleccionado ja existe na lista destino!", "warning", null, null, 3000);
                lista_autores.getSelectedItem().setSelected(false);
                return;
            }
        }

        SgAutor author = lista_autores.getSelectedItem().getValue();
        lista2.add(author);

        lista_autores2.setModel(lista2);
        lista_autores.getSelectedItem().setSelected(false);
    }

    @Listen("onTodoDelete =#lista_autores2")
    public void subtrairLista2(ForwardEvent evt) {
        Image img = (Image) evt.getOrigin().getTarget();

        Listitem litem = (Listitem) img.getParent().getParent();

        SgAutor autor = litem.getValue();
        lista2.remove(autor);
        lista_autores2.setModel(lista2);

    }

    @Listen("onClick = #add_author")
    public void adicionarAutor() {
        String author = autor_search.getValue();
        if (Strings.isBlank(author)) {
            Clients.showNotification("Informe o nome do autor!", "warning", null, null, 3000);
        } else if (isAutor(author)) {
            Clients.showNotification("O autor ja existe!", "warning", null, null, 3000);
        } else {
            SgAutor au = new SgAutor();
            au.setIdautor(Long.MIN_VALUE);
            au.setNome(author);
            try {
                new SgAutorJpaController(new JPA().getEmf()).create(au);
                Clients.showNotification("Autor adicionado com sucesso!", "info", null, null, 3000);
                lista2.add(au);
                lista_autores2.setModel(lista2);
            } catch (Exception ex) {
                Clients.showNotification("Nao foi possivel efectuar a operacao!", "warning", null, null, 3000);
            }
        }

    }

    public boolean isAutor(String author) {
        ListModelList<SgAutor> autores = new ListModelList<SgAutor>(new SgAutorJpaController(new JPA().getEmf()).findSgAutorEntities());
        for (SgAutor a : autores) {
            if (a.getNome() != null) {
                if (a.getNome().equalsIgnoreCase(author)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Listen("onSelect = #list_tipo")
    public void tipoObra() {
        String tipo = list_tipo.getSelectedItem().getLabel();
        if (tipo.equals("Livro")) {
            vol.setVisible(true);
            isb.setVisible(true);
            edit.setVisible(true);
            local_edic.setVisible(true);
            edic.setVisible(true);
            ano_pub.setVisible(true);
            cur.setVisible(false);
            nom.setVisible(false);
        }
        if (tipo.equals("Monografia")) {
            vol.setVisible(false);
            isb.setVisible(false);
            edit.setVisible(false);
            edic.setVisible(false);
            cur.setVisible(true);
            nom.setVisible(false);
        }
        if (tipo.equals("Revista") || tipo.equals("Jornal")) {
            edit.setVisible(true);
            local_edic.setVisible(true);
            edic.setVisible(true);
            ano_pub.setVisible(true);
            vol.setVisible(true);
            nom.setVisible(true);
            isb.setVisible(false);
            cur.setVisible(false);
        }
    }

    @Listen("onClick = #guardar")
    public void registarObra() {
        String tipoObra = list_tipo.getSelectedItem().getLabel();
        SgObra obra = new SgObra();
        //=================================================================================
        //                          No caso de ser um livro
        //=================================================================================
        if (tipoObra.equals("Livro")) {
            if (titulo.getValue() != null || volume.getValue() != null || isbn.getValue() != null
                    || cota.getValue() != null || editora.getValue() != null || edicao.getValue() != null
                    || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
                obra.setVolume(volume.getValue());
              
                obra.setIsbn(isbn.getValue());
                obra.setCota(cota.getValue());
                obra.setEditora(editora.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
                obra.setEdicao(edicao.getValue());
                obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setCadastroData(new Date());
                obra.setTipoObra(list_tipo.getSelectedItem().getLabel());
                if (!rd_directory.isChecked()) {
                    obra.setFormato("Fisico");
                } else {
                    obra.setFormato("Electronico");
                }
                obra.setBibliotecario(currentUser);
            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //=================================================================================
        //                          No caso de ser uma monografia
        //=================================================================================
        if (tipoObra.equals("Monografia")) {
            if (titulo.getValue() != null || cota.getValue() != null || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setCota(cota.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
               obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setTipoObra(list_tipo.getSelectedItem().getLabel());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
                obra.setCurso((Curso) combo_curso.getSelectedItem().getValue());
                obra.setCadastroData(new Date());
                if (!rd_directory.isChecked()) {
                    obra.setFormato("Fisico");
                } else {
                    obra.setFormato("Electronico");
                }
                obra.setBibliotecario(currentUser);

            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //=================================================================================
        //                          No caso de ser uma revista
        //=================================================================================
        if (tipoObra.equals("Revista") || tipoObra.equals("Jornal")) {
            if (titulo.getValue() != null || volume.getValue() != null || nome.getValue() != null
                    || cota.getValue() != null || editora.getValue() != null || edicao.getValue() != null
                    || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
              obra.setVolume(volume.getValue());
                obra.setNome(nome.getValue());
                obra.setCota(cota.getValue());
                obra.setEditora(editora.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
                obra.setEdicao(edicao.getValue());
               obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setCadastroData(new Date());
                obra.setTipoObra(list_tipo.getSelectedItem().getLabel());
                if (!rd_directory.isChecked()) {
                    obra.setFormato("Fisico");
                } else {
                    obra.setFormato("Electronico");
                }
                obra.setBibliotecario(currentUser);
            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //==========================================================================
        //===========================Registo do livro===============================
        //==========================================================================
        try {
            new SgObraJpaController(new JPA().getEmf()).create(obra);
        } catch (Exception ex) {
            Clients.showNotification("nao foi possivel registar a obra", "warning", null, null, 3000);
            return;
        }
        //=========================Adicao de autores============================
        SgObraAutor oa = new SgObraAutor();
        for (SgAutor aut : lista2) {
            SgObraAutorPK sgoapk = new SgObraAutorPK(aut.getIdautor(), obra.getIdlivro(), new Date());
            oa.setSgObraAutorPK(sgoapk);
            try {
                new SgObraAutorJpaController(new JPA().getEmf()).create(oa);
            } catch (Exception ex) {
                Clients.showNotification("nao foi possivel adicionar o autor " + aut.getNome(), "warning", null, null, 3000);
                return;
            }
        }
        //============================Adicao de exemplares==========================
        SgExemplar exempl = new SgExemplar();
        exempl.setDataAquisicao(dataAquisicao.getValue());
        exempl.setDataRegisto(new Date());
        exempl.setForma(comboAquisicao.getSelectedItem().getLabel());
        exempl.setAgenteRegisto(currentUser);
        exempl.setObraRef(obra);
        for (int i = 0; i < Integer.parseInt(exemplares.getValue()); i++) {
            exempl.setNrRegisto(Long.MIN_VALUE);
            if (isFixo(obra)) {
                exempl.setEstado("Disponivel");
            } else {
                exempl.setEstado("Fixo");
            }
            try {
                new SgExemplarJpaController(new JPA().getEmf()).create(exempl);
                Clients.showNotification("Registo efectuado com sucesso", "info", null, null, 3000);
            } catch (Exception ex) {
                Clients.showNotification("nao foi possivel adicionar os exemplares", "warning", null, null, 3000);
                return;
            }
        }
    }

    @Listen("onChanging = #exemplares")
    public void mostrarExtraCampos(InputEvent event) {
        String frase = event.getValue();
        if (Strings.isBlank(frase)) {
            extra.setVisible(false);
        } else {
            extra.setVisible(true);
        }
    }

    public Boolean isFixo(SgObra o) {
        ListModelList<SgExemplar> ex = new ListModelList<SgExemplar>(new SgExemplarJpaController(new JPA().getEmf()).findSgExemplarEntities());
        if (ex.isEmpty()) {
            return true;
        }
        for (SgExemplar sgExemplar : ex) {
            if (sgExemplar.getObraRef().equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Listen("onUpload=#up")
    public void carregarPDF(UploadEvent upEvent) throws IOException {

        //lbl.setValue("" + upEvent.getMedia());
        //media = Fileupload.get();
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
        divLivro.setVisible(false);
        divEdicao.setVisible(false);
        painelExemplar.setVisible(false);
        if (Strings.isBlank(frase)) {
            divLivro.setVisible(false);
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
        divLivro.setVisible(true);
        painelLivro.setVisible(true);
        mostrarLivro(Obra);
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
        formatoLivro.setValue(O.getFormato());
        if (qtdExemplares(O) != 0) {
            String qtd = "" + qtdExemplares(O);
            exemplaresLivro.setValue(qtd);
        } else {
            exemplaresLivro.setValue("Nenhum");
        }
        if (O.getTipoObra().equals("Livro")) {
            nomeLabel.setVisible(false);
            nomeJornal.setVisible(false);
            isbnLabel.setVisible(true);
            volumeLabel.setVisible(true);
            volumeLivro.setVisible(true);
            edicaoLivro.setVisible(true);
            editoraLivro.setVisible(true);
            edicaoLabel.setVisible(true);
            editoraLabel.setVisible(true);
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
            volumeLivro.setVisible(true);
            edicaoLivro.setVisible(true);
            editoraLivro.setVisible(true);
            edicaoLabel.setVisible(true);
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
    
    public void mostrarExemplar(SgExemplar exemplar) {
        tituloLivroExe.setValue(exemplar.getObraRef().getTitulo());
        cotaLivroExe.setValue(exemplar.getObraRef().getCota());
        anoLivroExe.setValue(exemplar.getObraRef().getPublicacaoAno().toString());
        tipoLivroExe.setValue(exemplar.getObraRef().getTipoObra());
        areaLivroExe.setValue(exemplar.getObraRef().getArea().getDescricao());
        idiomaLivroExe.setValue(exemplar.getObraRef().getIdioma());
        categoriaLivroExe.setValue(exemplar.getObraRef().getDominio().getDescricao());
        dataRegistoLivroExe.setValue(formatter.format(exemplar.getDataRegisto()));
        dataAquisicaoLivroExe.setValue(formatter.format(exemplar.getDataAquisicao()));
        nrRegisto.setValue(exemplar.getNrRegisto().toString());
        if(exemplar.getEstado() != null && exemplar.getEstado().equals("Fixo")){
            estadoExe.setValue(exemplar.getEstado());
            estadoExe.setSclass("label label-info");
        }
        if(exemplar.getEstado() != null && exemplar.getEstado().equals("Disponivel")){
            estadoExe.setValue(exemplar.getEstado());
            estadoExe.setSclass("label label-success");
        }
        if(exemplar.getEstado() != null && exemplar.getEstado().equals("Emprestado")){
            estadoExe.setValue(exemplar.getEstado());
            estadoExe.setSclass("label label-primary");
        }
        if (qtdExemplares(exemplar.getObraRef()) != 0) {
            String qtd = "" + qtdExemplares(exemplar.getObraRef());
            exemplaresLivroExe.setValue(qtd);
        } else {
            exemplaresLivroExe.setValue("Nenhum");
        }
        if (exemplar.getObraRef().getTipoObra().equals("Livro")) {
            nomeLabelExe.setVisible(false);
            nomeJornalExe.setVisible(false);
            isbnLabelExe.setVisible(true);
            volumeLabelExe.setVisible(true);
            volumeLivroExe.setVisible(true);
            edicaoLivroExe.setVisible(true);
            editoraLivroExe.setVisible(true);
            edicaoLabelExe.setVisible(true);
            editoraLabelExe.setVisible(true);
            volumeLivroExe.setValue(exemplar.getObraRef().getVolume().toString());
            edicaoLivroExe.setValue(exemplar.getObraRef().getEdicao().toString());
            isbnLivroExe.setValue(exemplar.getObraRef().getIsbn());
            editoraLivroExe.setValue(exemplar.getObraRef().getEditora());
            localEdicaoLivroExe.setValue(exemplar.getObraRef().getEdicaoCidade());
        }
        if (exemplar.getObraRef().getTipoObra().equals("Jornal") || exemplar.getObraRef().getTipoObra().equals("Revista")) {
            nomeLabelExe.setVisible(true);
            nomeJornalExe.setVisible(true);
            isbnLabelExe.setVisible(false);
            volumeLabelExe.setVisible(true);
            volumeLivroExe.setVisible(true);
            edicaoLivroExe.setVisible(true);
            editoraLivroExe.setVisible(true);
            edicaoLabelExe.setVisible(true);
            editoraLabelExe.setVisible(true);
            nomeJornalExe.setValue(exemplar.getObraRef().getNome());
            volumeLivroExe.setValue(exemplar.getObraRef().getVolume().toString());
            edicaoLivroExe.setValue(exemplar.getObraRef().getEdicao().toString());
            isbnLivroExe.setValue(exemplar.getObraRef().getIsbn());
            editoraLivroExe.setValue(exemplar.getObraRef().getEditora());
            localEdicaoLivroExe.setValue(exemplar.getObraRef().getEdicaoCidade());
        }
        if (exemplar.getObraRef().getTipoObra().equals("Monografia")) {
            nomeLabelExe.setVisible(false);
            nomeJornalExe.setVisible(false);
            volumeLivroExe.setVisible(false);
            isbnLabelExe.setVisible(false);
            volumeLabelExe.setVisible(false);
            edicaoLabelExe.setVisible(false);
            edicaoLivroExe.setVisible(false);
            editoraLabelExe.setVisible(false);
            editoraLivroExe.setVisible(false);
            localEdicaoLivroExe.setValue(exemplar.getObraRef().getEdicaoCidade());
            
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

    @Listen("onClick = #Addexe")
    public void adicionarExemplar() {
        if (qtdExemplares.getValue() == null || dataAquis.getValue() == null || comboAquis.getSelectedItem().getLabel() == null) {
            Clients.showNotification("Preencha todos os campos", "warning", null, null, 3000);
        } else {
            SgExemplar exempl = new SgExemplar();
            exempl.setDataAquisicao(dataAquis.getValue());
            exempl.setDataRegisto(new Date());
            exempl.setForma(comboAquis.getSelectedItem().getLabel());
            exempl.setAgenteRegisto(currentUser);
            exempl.setObraRef(Obra);
            for (int i = 0; i < Integer.parseInt(qtdExemplares.getValue()); i++) {
                exempl.setNrRegisto(Long.MIN_VALUE);
                if (isFixo(Obra)) {
                    exempl.setEstado("Disponivel");
                } else {
                    exempl.setEstado("Fixo");
                }
                try {
                    new SgExemplarJpaController(new JPA().getEmf()).create(exempl);
                    String qtd = "" + qtdExemplares(Obra);
                    exemplaresLivro.setValue(qtd);
                    Clients.showNotification("Registo efectuado com sucesso", "info", null, null, 3000);
                } catch (Exception ex) {
                    Clients.showNotification("nao foi possivel adicionar os exemplares", "warning", null, null, 3000);
                    return;
                }
            }
        }
    }
    
    public void tipoObra(SgObra book) {
        titulo.setValue(book.getTitulo());
        cota.setValue(book.getCota());
        ano_publicacao.setValue(book.getPublicacaoAno().toString());
        local_edicao.setValue(book.getEdicaoCidade());
        SgObra ob = new SgObraJpaController(new JPA().getEmf()).findSgObra(book.getIdlivro());
        ListModelList<SgObraAutor> listaAut = new ListModelList<SgObraAutor>(ob.getSgObraAutorList());
        lista2.clear();
        for (SgObraAutor obrautor : listaAut) {
            lista2.add(obrautor.getSgAutor());
        }
        lista_autores2.setModel(lista2);           
        if (book.getTipoObra().equals("Livro")) {
            vol.setVisible(true);
            volume.setValue(book.getVolume().toString());
            edit.setVisible(true);
            editora.setValue(book.getEditora());
            edic.setVisible(true);
            edicao.setValue(book.getEdicao().toString());
            cur.setVisible(false);
            nom.setVisible(false);
        }
        if (book.getTipoObra().equals("Monografia")) {
            vol.setVisible(false);
            edit.setVisible(false);
            edic.setVisible(false);
            cur.setVisible(true);
            nom.setVisible(false);
        }
        if (book.getTipoObra().equals("Revista") || book.getTipoObra().equals("Jornal")) {
            edit.setVisible(true);
            editora.setValue(book.getEditora());
            edic.setVisible(true);
            edicao.setValue(book.getEdicao().toString());
            vol.setVisible(true);
            volume.setValue(book.getVolume().toString());
            nom.setVisible(true);
            nome.setValue(book.getNome());
            cur.setVisible(false);
        }
    }
    
    @Listen("onClick = #editarObra")
    public void mostrarDivEdicao(){
        divLivro.setVisible(false);
        divEdicao.setVisible(true);
        
        tipoObra(Obra);
    }
    
    @Listen("onClick = #guardarEdicao")
    public void editarObra(){
        String tipoObra = Obra.getTipoObra();
        SgObra obra = new SgObraJpaController(new JPA().getEmf()).findSgObra(Obra.getIdlivro());
        //=================================================================================
        //                          No caso de ser um livro
        //=================================================================================
        if (tipoObra.equals("Livro")) {
            if (titulo.getValue() != null || volume.getValue() != null || isbn.getValue() != null
                    || cota.getValue() != null || editora.getValue() != null || edicao.getValue() != null
                    || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
       obra.setVolume(volume.getValue());
                obra.setCota(cota.getValue());
                obra.setEditora(editora.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
               obra.setEdicao(edicao.getValue());
               obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setBibliotecario(currentUser);
            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //=================================================================================
        //                          No caso de ser uma monografia
        //=================================================================================
        if (tipoObra.equals("Monografia")) {
            if (titulo.getValue() != null || cota.getValue() != null || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setCota(cota.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
               obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
                obra.setCurso((Curso) combo_curso.getSelectedItem().getValue());
                obra.setBibliotecario(currentUser);

            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //=================================================================================
        //                          No caso de ser uma revista
        //=================================================================================
        if (tipoObra.equals("Revista") || tipoObra.equals("Jornal")) {
            if (titulo.getValue() != null || volume.getValue() != null || nome.getValue() != null
                    || cota.getValue() != null || editora.getValue() != null || edicao.getValue() != null
                    || local_edicao.getValue() != null || ano_publicacao.getValue() != null) {
                obra.setIdlivro(Long.MIN_VALUE);
                obra.setTitulo(titulo.getValue());
                obra.setDominio((SgObraCategoria) combo_categoria.getSelectedItem().getValue());//Categoria
                obra.setVolume(volume.getValue());
                obra.setNome(nome.getValue());
                obra.setCota(cota.getValue());
                obra.setEditora(editora.getValue());
                obra.setEdicaoCidade(local_edicao.getValue());
                obra.setIdioma(comboidioma.getSelectedItem().getValue().toString());
                obra.setArea((SgObraArea) combo_area.getSelectedItem().getValue());
               obra.setEdicao(edicao.getValue());
                obra.setPublicacaoAno(ano_publicacao.getValue());
                obra.setBibliotecario(currentUser);
            } else {
                Clients.showNotification("Preencha todos os campos!", "warning", null, null, 3000);
                return;
            }
        }
        //==========================================================================
        //===========================Registo do livro===============================
        //==========================================================================
        try {
            new SgObraJpaController(new JPA().getEmf()).edit(obra);
        } catch (Exception ex) {
            Clients.showNotification("nao foi possivel registar a obra", "warning", null, null, 3000);
            return;
        }
        //=========================Adicao de autores============================
        
        for (SgAutor aut : lista2) {
            SgObraAutorPK sgoapk = new SgObraAutorPK(aut.getIdautor(), obra.getIdlivro(), new Date());
            SgObraAutor oa = new SgObraAutorJpaController(new JPA().getEmf()).findSgObraAutor(sgoapk);
            oa.setSgObraAutorPK(sgoapk);
            try {
                new SgObraAutorJpaController(new JPA().getEmf()).edit(oa);
            } catch (Exception ex) {
                Clients.showNotification("nao foi possivel adicionar o autor " + aut.getNome(), "warning", null, null, 3000);
                return;
            }
        }
        Clients.showNotification("Obra editada com sucesso", "info", null, null, 3000);
    }
    
    @Listen("onChanging = #nrExemplarSearch")
    public void searchByNrExemplar(InputEvent event){
        String numero = event.getValue();
        painelLivro.setVisible(false);
        painelExemplar.setVisible(false);
        divEdicao.setVisible(false);
        if(Strings.isBlank(numero)){
            divLivro.setVisible(false);
        } else {
            SgExemplar exe = new SgExemplarJpaController(new JPA().getEmf()).findSgExemplar(Long.parseLong(numero));
            if(exe == null)
                Clients.showNotification("Exemplar não encontrado", "warning", nrExemplarSearch, "after_start", 3000);
            else {
                Obra = exe.getObraRef();
                exemp = exe;
                mostrarExemplar(exemp);
                painelExemplar.setVisible(true);
                divLivro.setVisible(true);
            }
        }
    }
    
}
