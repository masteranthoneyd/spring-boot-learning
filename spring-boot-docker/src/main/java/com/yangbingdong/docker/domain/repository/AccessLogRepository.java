package com.yangbingdong.docker.domain.repository;

import com.yangbingdong.docker.domain.core.root.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
