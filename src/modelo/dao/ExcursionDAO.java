package modelo.dao;

import modelo.Excursion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class ExcursionDAO {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public ExcursionDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("nombre_de_unidad_de_persistencia");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void agregarExcursion(Excursion excursion) {
        entityManager.getTransaction().begin();
        entityManager.persist(excursion);
        entityManager.getTransaction().commit();
    }

    public boolean eliminarExcursion(int idExcursion) {
        Excursion excursion = entityManager.find(Excursion.class, idExcursion);
        if (excursion != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(excursion);
            entityManager.getTransaction().commit();
            return true;
        } else {
            return false;
        }
    }

    public List<Excursion> obtenerListaExcursiones() {
        Query query = entityManager.createQuery("SELECT e FROM Excursion e");
        return query.getResultList();
    }

    public Excursion buscarExcursionPorId(int idExcursion) {
        return entityManager.find(Excursion.class, idExcursion);
    }

    public void cerrarConexion() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
