package modelo.dao;

import modelo.Estandar;
import modelo.Federado;
import modelo.Infantil;
import modelo.Seguro;
import modelo.Socio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class SociosDAO {

    private final EntityManagerFactory entityManagerFactory;

    public SociosDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("unidadPersistencia");
    }

    public void agregarSocio(Socio socio) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(socio);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public boolean eliminarSocioPorId(int idSocio) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Socio socio = entityManager.find(Socio.class, idSocio);
            if (socio != null) {
                entityManager.remove(socio);
                transaction.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();
        }
    }

    public Socio buscarSocioPorId(int idSocio) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Socio.class, idSocio);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            entityManager.close();
        }
    }

    public List<Socio> obtenerListaSocios() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("FROM Socio", Socio.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }

    public List<Socio> obtenerListaSociosPorTipo(String tipoSocio) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("FROM Socio WHERE tipoSocio = :tipo", Socio.class)
                    .setParameter("tipo", tipoSocio)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }

    public void mostrarListaSocios(List<Socio> listaSocios) {
        if (!listaSocios.isEmpty()) {
            System.out.println("Lista de Socios:");
            for (Socio socio : listaSocios) {
                System.out.println(socio);
            }
        } else {
            System.out.println("No se encontraron Socios");
        }
    }
}
