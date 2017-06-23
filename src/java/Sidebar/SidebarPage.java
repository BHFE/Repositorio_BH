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
import java.io.Serializable;

public class SidebarPage implements Serializable {

    private static final long serialVersionUID = 1L;
    String name;
    String label;
    String image;
    String uri;

    public SidebarPage(String name, String label, String image, String uri) {
        this.name = name;
        this.label = label;
        this.image = image;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getUri() {
        return uri;
    }
}
