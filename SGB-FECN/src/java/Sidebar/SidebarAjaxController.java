/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sidebar;

/**
 *
 * @author Nobrega
 */
import conexao.JPA;
import controladores.entidades.UsersJpaController;
import entidades.Usergrupo;
import entidades.Users;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import servicos.Autenticacao;
import servicos.AutenticacaoImpl;
import servicos.UserCredential;

public class SidebarAjaxController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;
    @Wire
    Grid fnList;

    //wire service
    SidebarPageConfig pageConfig = new SidebarPageConfigAjax();
    Autenticacao authService = new AutenticacaoImpl();
    Users currentUser = new Users();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        UserCredential cre = authService.getUserCredential();
        this.currentUser = new UsersJpaController(new JPA().getEmf()).findUsers(cre.getAccount());

        //to initial view after view constructed.
        Rows rows = fnList.getRows();

        if (currentUser != null) {
            if (isGestor(currentUser)) {
                for (SidebarPage page : pageConfig.getPages()) {
                    Row row = constructSidebarRow(page.getName(), page.getLabel(), page.getImage(), page.getUri());
                    rows.appendChild(row);
                }
            } else {
                for (SidebarPage page : pageConfig.getPages()) {
                    if (!page.getLabel().equals("Administração")) {
                        Row row = constructSidebarRow(page.getName(), page.getLabel(), page.getImage(), page.getUri());
                        rows.appendChild(row);
                    }
                }
            }
        } else
                Executions.sendRedirect("/Paginas/login/login.zul");
    }

    private Row constructSidebarRow(final String name, String label, String imageSrc, final String locationUri) {

        //construct component and hierarchy
        Row row = new Row();
        Image image = new Image(imageSrc);
        Label lab = new Label(label);

        row.appendChild(lab);

        //set style attribute
//		row.setSclass("/css/sidebar-fn.css");
        //new and register listener for events
        EventListener<Event> onActionListener = new SerializableEventListener<Event>() {
            private static final long serialVersionUID = 1L;

            public void onEvent(Event event) throws Exception {
                //redirect current url to new location
                if (locationUri.startsWith("http")) {
                    //open a new browser tab
                    Executions.getCurrent().sendRedirect(locationUri);
                } else {
                    //use iterable to find the first include only
                    Include include = (Include) Selectors.iterable(fnList.getPage(), "#mainInclude")
                            .iterator().next();
                    include.setSrc(locationUri);

                    //advance bookmark control, 
                    //bookmark with a prefix
                    if (name != null) {
                        getPage().getDesktop().setBookmark("p_" + name);
                    }
                }
            }
        };
        row.addEventListener(Events.ON_CLICK, onActionListener);

        return row;
    }

    public Boolean isGestor(Users usr) {
        List<Usergrupo> listaGrupo = usr.getUsergrupoList();
        for (Usergrupo usergrupo : listaGrupo) {
            if (usergrupo.getUsergrupoPK().getIdGrupo().equals("Gestor Bibliotecario")) {
                return true;
            }
        }
        return false;
    }
}
