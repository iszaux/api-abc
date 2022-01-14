package com.roshka.daos;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.roshka.models.Tokens;

@Stateless
public class TokenDao extends GenericDao<Tokens> {

    @PersistenceContext(unitName = "com.roshka_api-abc-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TokenDao() {
        super(Tokens.class);
    }

    public Tokens finApiKey(String apikey) {
        TypedQuery<Tokens> query = em.createNamedQuery("Tokens.findByApikey", Tokens.class)
                .setParameter("apikey", apikey);
        List<Tokens> results = query.getResultList();
        if (results.size() > 0)
            return results.get(0);
        else
            return null;
    }

}
