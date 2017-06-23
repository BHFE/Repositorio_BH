/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.paginas;

import conexao.JPA;
import controladores.entidades.SgAutorJpaController;
import controladores.entidades.UsersJpaController;
import entidades.SgAutor;
import entidades.Users;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.lang.Strings;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

/**
 *
 * @author Nobrega
 */
public class AutorController extends SelectorComposer<Component> {

    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();

    @Wire
    Listbox lista_autores, lista_autores2;
    @Wire
    Div div_lista_autores;
    @Wire
    Textbox autor_search;
    @Wire
    Separator sep;

    //Declaracao da segunda lista
    ListModelList<SgAutor> lista2 = new ListModelList<SgAutor>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());

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
    public void adicionarAutor(){
        String author = autor_search.getValue();
        if(Strings.isBlank(author)){
            Clients.showNotification("Informe o nome do autor!", "warning", null, null, 3000);
        }
        else {
            if(isAutor(author))
                Clients.showNotification("O autor ja existe!", "warning", null, null, 3000);
            else{
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
        
    }
    
    public boolean isAutor(String author){
        ListModelList<SgAutor> autores = new ListModelList<SgAutor>(new SgAutorJpaController(new JPA().getEmf()).findSgAutorEntities());
        for (SgAutor a : autores) {
            if(a.getNome() != null){
                if(a.getNome().equalsIgnoreCase(author))
                    return true;
            }
        }
        return false;
    }
}
