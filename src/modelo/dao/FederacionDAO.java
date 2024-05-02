package modelo.dao;

import modelo.Federacion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class FederacionDAO {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public FederacionDAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit-name");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public Federacion obtenerFederacion(int idFederacion) {
        return entityManager.find(Federacion.class, idFederacion);
    }

    public Federacion obtenerFederacionPorNombre(String nombreFederacion) {
        Query query = entityManager.createQuery("SELECT f FROM Federacion f WHERE f.nombreFederacion = :nombre");
        query.setParameter("nombre", nombreFederacion);
        List<Federacion> federaciones = query.getResultList();
        if (!federaciones.isEmpty()) {
            return federaciones.get(0);
        } else {
            // Aquí puedes manejar el caso de que la federación no exista en la base de datos
            return null;
        }
    }

    public Federacion crearFederacion(String nombreFederacion) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Federacion federacion = new Federacion(nombreFederacion);
        entityManager.persist(federacion);

        transaction.commit();

        return federacion;
    }

    public void cerrarConexion() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
