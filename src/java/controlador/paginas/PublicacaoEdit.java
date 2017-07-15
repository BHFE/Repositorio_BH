/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.BvArtigoCategoriaJpaController;
import controladores.entidades.BvArtigoJpaController;
import controladores.entidades.SgObraAreaJpaController;
import controladores.entidades.UsersJpaController;
import entidades.BvArtigo;
import entidades.BvArtigoCategoria;
import entidades.SgObraArea;
import entidades.Users;
import java.util.Date;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Uazela
 */
public class PublicacaoEdit extends SelectorComposer<Component> {

    @Wire
    Component parent;

    @Wire
    Textbox titulo, descricao, direitos, autor;
    
    @Wire
    Listbox categorias;

    @Wire
    private Listbox area;

    ListModelList<BvArtigoCategoria> categoria;
    ListModelList<SgObraArea> areas;

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();

    BvArtigo artigoR;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        //Identificando o utilizador actual
        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());

        //Preenchedo Tabelas
        categoria = new ListModelList<BvArtigoCategoria>(new BvArtigoCategoriaJpaController(new JPA().getEmf()).findBvArtigoCategoriaEntities());
        areas = new ListModelList<SgObraArea>(new SgObraAreaJpaController(new JPA().getEmf()).findSgObraAreaEntities());
        categorias.setModel(categoria);
        area.setModel(areas);

        final Execution execution = Executions.getCurrent();
        artigoR = (BvArtigo) execution.getArg().get("artigo");

        preencherDados(artigoR);

    }
    
    @Listen("onClick = #cancelar")
	public void showModal(Event e) {
		parent.detach();
	}

    public void preencherDados(BvArtigo artigo) {

        autor.setText(artigo.getAutor());
        titulo.setText(artigo.getTitulo());

        descricao.setValue(artigo.getDescricao());
        direitos.setValue(artigo.getDireitos());

        ((ListModelList) area.getModel()).addToSelection(artigo.getArea());

        ((ListModelList) categorias.getModel()).addToSelection(artigo.getTipodoc());

    }

    @Listen("onClick=#editar")
    public void editando() {
        publicar(artigoR);
    }

    public void publicar(BvArtigo artigo) {
        //Editando na base de dados  
        artigo.setAutor(autor.getValue());
        artigo.setTitulo(titulo.getValue());
        artigo.setDirectorioPdf(artigo.getDirectorioPdf());
        artigo.setPublicador(artigo.getPublicador());
        artigo.setDataPublicacao(new Date());
        artigo.setIdioma("Portugues");
        artigo.setFormato(artigo.getFormato());

        Set<SgObraArea> sele = ((ListModelList) area.getModel()).getSelection();
        if (!sele.isEmpty()) {
            artigo.setArea(sele.iterator().next());
        } else {
            artigo.setArea(null);
        }

        Set<BvArtigoCategoria> selection = ((ListModelList) categorias.getModel()).getSelection();
        if (!selection.isEmpty()) {
            artigo.setTipodoc(selection.iterator().next());
        } else {
            artigo.setTipodoc(null);
        }

        try {
            new BvArtigoJpaController(new JPA().getEmf()).edit(artigo);

            Clients.showNotification("Publicacao Modificada com sucesso");
            Executions.getCurrent().sendRedirect("/Paginas/leitor/publicacaoList.zul");
        } catch (Exception ex) {
            Clients.showNotification("Erro na Modificacao");
        }

    }

}
