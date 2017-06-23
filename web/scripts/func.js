/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function mudarEstado(el){
    var display = document.getElementById(el).style.display;
    if(display === "none"){
        document.getElementById(el).style.display = "block";
    }
    else {
        document.getElementById(el).style.display = "none";
    }
}