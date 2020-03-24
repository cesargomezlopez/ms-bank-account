package ms.bank.account.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICrud<T,S> {

  Flux<T> findAll();

  Mono<T> findById(S id);

  Mono<T> create(T entity);

  Mono<T> update(T entity);

  Mono<Void> deleteById(S id);

}
