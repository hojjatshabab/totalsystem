package fava.betaja.erp.repository.baseinfo.extra.impl;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import fava.betaja.erp.enums.baseinfo.CartableState;
import fava.betaja.erp.repository.baseinfo.extra.CartableExtraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartableExtraRepositoryImpl implements CartableExtraRepository {

    private final EntityManager entityManager;


    @Override
    public ImmutablePair<Long, List<Cartable>> findNotApproveCartableByUser(Long userId, PageRequest<CartableDto> model) {

        String baseQuery = "FROM CartableHistory h JOIN h.cartable c " +
                "WHERE h.user.id = :userId " +
                "AND c.state <> :approvedState ";

        Query querySearch = entityManager.createQuery("SELECT DISTINCT c " + baseQuery + "ORDER BY c.modificationDateTime DESC", Cartable.class);
        Query queryCount = entityManager.createQuery("SELECT COUNT(DISTINCT c.id) " + baseQuery);

        querySearch.setParameter("userId", userId);
        querySearch.setParameter("approvedState", CartableState.APPROVED);
        queryCount.setParameter("userId", userId);
        queryCount.setParameter("approvedState", CartableState.APPROVED);

        int firstResult = (model.getCurrentPage() - 1) * model.getPageSize();
        querySearch.setFirstResult(firstResult);
        querySearch.setMaxResults(model.getPageSize());

        List<Cartable> cartables = querySearch.getResultList();
        Long total = (Long) queryCount.getSingleResult();

        return new ImmutablePair<>(total, cartables);
    }

    @Override
    public ImmutablePair<Long, List<Cartable>> findApproveCartableByUser(Long userId, PageRequest<CartableDto> model) {

        String baseQuery = "FROM CartableHistory h JOIN h.cartable c " +
                "WHERE h.user.id = :userId " +
                "AND c.state = :approvedState ";

        Query querySearch = entityManager.createQuery("SELECT DISTINCT c " + baseQuery + "ORDER BY c.modificationDateTime DESC", Cartable.class);
        Query queryCount = entityManager.createQuery("SELECT COUNT(DISTINCT c.id) " + baseQuery);

        querySearch.setParameter("userId", userId);
        querySearch.setParameter("approvedState", CartableState.APPROVED);
        queryCount.setParameter("userId", userId);
        queryCount.setParameter("approvedState", CartableState.APPROVED);

        int firstResult = (model.getCurrentPage() - 1) * model.getPageSize();
        querySearch.setFirstResult(firstResult);
        querySearch.setMaxResults(model.getPageSize());

        List<Cartable> cartables = querySearch.getResultList();
        Long total = (Long) queryCount.getSingleResult();

        return new ImmutablePair<>(total, cartables);
    }

    @Override
    public ImmutablePair<Long, List<Cartable>> findAllCartableByUser(Long userId, PageRequest<CartableDto> model) {

        String baseQuery = "FROM CartableHistory h JOIN h.cartable c " +
                "WHERE h.user.id = :userId " ;

        Query querySearch = entityManager.createQuery("SELECT DISTINCT c " + baseQuery + "ORDER BY c.modificationDateTime DESC", Cartable.class);
        Query queryCount = entityManager.createQuery("SELECT COUNT(DISTINCT c.id) " + baseQuery);

        querySearch.setParameter("userId", userId);
        queryCount.setParameter("userId", userId);

        int firstResult = (model.getCurrentPage() - 1) * model.getPageSize();
        querySearch.setFirstResult(firstResult);
        querySearch.setMaxResults(model.getPageSize());

        List<Cartable> cartables = querySearch.getResultList();
        Long total = (Long) queryCount.getSingleResult();

        return new ImmutablePair<>(total, cartables);
    }

}

