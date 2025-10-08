/**
 * WebFluxを使っている場合のCSRF対策の設定.
 * 参考サイトにある、resolveCsrfTokenメソッドの通り実装すると、access deniedになる.
 * 逆にresolveCsrfTokenを実装せずデフォルトのままにすると、機体通りに動く.
 * <p>
 * 原因は不明.
 * <p>
 * https://github.com/spring-projects/spring-security/issues/14125
 */
package com.polarbookshop.edge_service.security.csrf;