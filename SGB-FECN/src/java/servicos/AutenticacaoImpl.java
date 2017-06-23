/* 
 Description:
 ZK Essentials
 History:
 Created by dennis

 Copyright (C) 2012 Potix Corporation. All Rights Reserved.
 */
package servicos;

import conexao.JPA;
import controladores.entidades.BLeitorJpaController;
import controladores.entidades.UsersJpaController;
import entidades.BLeitor;
import entidades.Usergrupo;

import entidades.Users;
import java.io.Serializable;
import java.util.List;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

public class AutenticacaoImpl implements Autenticacao, Serializable {

    private static final long serialVersionUID = 1L;

    public UserCredential getUserCredential() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
        if (cre == null) {
            cre = new UserCredential();//new a anonymous user and set to session
            sess.setAttribute("userCredential", cre);

        }
        return cre;
    }

    public int login(String nm, String pd) {
        Users user = new UsersJpaController(new JPA().getEmf()).findUsers(nm);

        if (user == null || !user.getPasword().equals(pd)) {
            return 1;
            //utilizador nao existe ou password incorrecto
        }
        if (user.getEstado() != null) {
            if (user.getEstado().equals("Inactivo")) {
                return 2;
                //o utilizador esta bloqueado
            }
        }

        List<Usergrupo> listaGrupo = user.getUsergrupoList();
        for (Usergrupo usergrupo : listaGrupo) {
            if (usergrupo.getUsergrupoPK().getIdGrupo().equals("Bibliotecario") && usergrupo.getUsergrupoPK().getIdGrupo().equals("Gestor Bibliotecario")) {
                Session sess = Sessions.getCurrent();
                UserCredential cre = new UserCredential(user.getUtilizador(), user.getNome());
                sess.setAttribute("userCredential", cre);
                return 3;
                //o utilizador tem previlegios de administrador
            } else if (usergrupo.getUsergrupoPK().getIdGrupo().equals("Bibliotecario")) {
                Session sess = Sessions.getCurrent();
                UserCredential cre = new UserCredential(user.getUtilizador(), user.getNome());
                sess.setAttribute("userCredential", cre);
                return 4;
            } //o utilizador tem previlegios de bibliotecario
            else if (usergrupo.getUsergrupoPK().getIdGrupo().equals("Gestor Bibliotecario")) {
                Session sess = Sessions.getCurrent();
                UserCredential cre = new UserCredential(user.getUtilizador(), user.getNome());
                sess.setAttribute("userCredential", cre);
                return 3;
            }
        }
        Session sess = Sessions.getCurrent();
        UserCredential cre = new UserCredential(user.getUtilizador(), user.getNome());
        sess.setAttribute("userCredential", cre);
        if (cre.isAnonymous()) {
            return 5;
        }
        return 6;
    }

    public void logout() {
        Session sess = Sessions.getCurrent();
        sess.removeAttribute("userCredential");
    }

    public BLeitor pegarLeitor(Users user) {
        ListModelList<BLeitor> leitores = new ListModelList<BLeitor>(new BLeitorJpaController(new JPA().getEmf()).findBLeitorEntities());
        BLeitor l = new BLeitor();
        for (BLeitor leitor : leitores) {
            if (leitor.getIdutilizador() != null) {
                if (leitor.getIdutilizador().equals(user)) {
                    l = leitor;
                    return l;
                }
            }
        }
        return l;
    }
}
