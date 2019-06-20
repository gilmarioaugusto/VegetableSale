package e.tux.ifruit;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private boolean vendendor;
    private boolean administrador;

    public Usuario() {
    }

    public Usuario(String email, boolean vendendor, boolean administrador) {
        this.email = email;
        this.vendendor = vendendor;
        this.administrador = administrador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVendendor() {
        return vendendor;
    }

    public void setVendendor(boolean vendendor) {
        this.vendendor = vendendor;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
}
