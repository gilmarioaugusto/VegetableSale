package e.tux.ifruit;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private boolean vendedor;
    private boolean administrador;

    public Usuario() {
    }

    public Usuario(String email, boolean vendendor, boolean administrador) {
        this.email = email;
        this.vendedor = vendendor;
        this.administrador = administrador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVendedor() {
        return vendedor;
    }

    public void setVendedor(boolean vendendor) {
        this.vendedor = vendendor;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
}
