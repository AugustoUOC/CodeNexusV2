package modelo.dao;

import modelo.Inscripcion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InscripcionDAO {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public InscripcionDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("nombre_de_la_unidad_de_persistencia");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        entityManager.getTransaction().begin();
        entityManager.persist(inscripcion);
        entityManager.getTransaction().commit();
        System.out.println("La inscripción se ha agregado correctamente.");
    }

    public Inscripcion buscarInscripcionPorID(int idInscripcion) {
        return entityManager.find(Inscripcion.class, idInscripcion);
    }

    public void eliminarInscripcion(int idInscripcion) {
        entityManager.getTransaction().begin();
        Inscripcion inscripcion = entityManager.find(Inscripcion.class, idInscripcion);
        if (inscripcion != null) {
            entityManager.remove(inscripcion);
            System.out.println("La inscripción se ha eliminado correctamente.");
        } else {
            System.out.println("No se encontró la inscripción con ID: " + idInscripcion);
        }
        entityManager.getTransaction().commit();
    }

    public List<Inscripcion> obtenerListaInscripciones() {
        Query query = entityManager.createQuery("SELECT i FROM Inscripcion i", Inscripcion.class);
        return query.getResultList();
    }

    public List<Inscripcion> obtenerInscripcionesPorSocio(int idSocio) {
        Query query = entityManager.createQuery("SELECT i FROM Inscripcion i WHERE i.idSocio = :idSocio", Inscripcion.class);
        query.setParameter("idSocio", idSocio);
        return query.getResultList();
    }

    public List<Inscripcion> obtenerInscripcionesFiltroFechas(Date fechaInicio, Date fechaFin) {
        Query query = entityManager.createQuery("SELECT i FROM Inscripcion i WHERE i.fechaInscripcion BETWEEN :fechaInicio AND :fechaFin", Inscripcion.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    public void cerrarConexion() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
