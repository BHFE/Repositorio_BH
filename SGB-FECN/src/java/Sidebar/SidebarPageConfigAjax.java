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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class SidebarPageConfigAjax implements SidebarPageConfig{
    
    HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
    
	public SidebarPageConfigAjax(){		
				
		pageMap.put("fn1",new SidebarPage("fn1","Gerir utente","","/Paginas/Utente.zul"));
		pageMap.put("fn2",new SidebarPage("fn2","Gerir obra","","/Paginas/Catalogacao.zul"));
		pageMap.put("fn3",new SidebarPage("fn3","Circulação","","/Paginas/Emprestimo.zul"));
                pageMap.put("fn4",new SidebarPage("fn4","Administração","","/Paginas/Administracao.zul"));
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
}
