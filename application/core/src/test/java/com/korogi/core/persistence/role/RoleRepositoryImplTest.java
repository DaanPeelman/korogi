package com.korogi.core.persistence.role;

import static org.fest.assertions.Assertions.assertThat;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.korogi.core.domain.Role;
import com.korogi.core.persistence.BaseRepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Daan Peelman
 */
public class RoleRepositoryImplTest extends BaseRepositoryTest {
    @Autowired
    private RoleRepositoryImpl repository;

    /**
     * Should retrieve the Role with the same id from the database.
     */
    @Test
    @DatabaseSetup("/com/korogi/core/persistence/role/RoleRepositoryTest_testFindById.xml")
    public void testFindById() throws Exception {
        long idToFind = 1;

        Role foundRole = repository.findById(idToFind);

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.id()).isEqualTo(idToFind);
    }
}