package modelo.dao;

import modelo.Seguro;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SegurosDAO {

    private EntityManagerFactory emf;

    public SegurosDAO() {
        emf = Persistence.createEntityManagerFactory("nombre_de_tu_persistencia");
    }

    public Seguro obtenerSeguro(int idSeguro) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Seguro.class, idSeguro);
        } finally {
            em.close();
        }
    }

    public void actualizarSeguroDeSocio(Seguro seguro) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(seguro);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        emf.close();
    }
}
