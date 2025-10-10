package fava.betaja.erp.repository.baseinfo.extra;

import fava.betaja.erp.dto.PageRequest;
import fava.betaja.erp.dto.baseinfo.CartableDto;
import fava.betaja.erp.entities.baseinfo.Cartable;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

public interface CartableExtraRepository {
    ImmutablePair<Long, List<Cartable>> findNotApproveCartableByUser(Long userId, PageRequest<CartableDto> model);
    ImmutablePair<Long, List<Cartable>> findApproveCartableByUser(Long userId, PageRequest<CartableDto> model);
    ImmutablePair<Long, List<Cartable>> findAllCartableByUser(Long userId, PageRequest<CartableDto> model);
}
