package net.leoninja.javaoracleverification.sampleoraclesjis

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked

class SampleRepository(
    private val jdbi: Jdbi
) {
    fun findAll(): List<Sample> {
        return jdbi.withHandleUnchecked { handle ->
            return@withHandleUnchecked handle.createQuery("SELECT id, str1, str2 FROM sample ORDER BY id")
                .mapTo(Sample::class.java)
                .list()
        }
    }

    fun create(sample: Sample): Int {
        return jdbi.withHandleUnchecked { handle ->
            return@withHandleUnchecked handle.createUpdate("INSERT INTO sample (id, str1, str2) VALUES (:id, :str1, :str2)")
                .bindBean(sample)
                .execute()
        }
    }

    fun deleteAll(): Int {
        return jdbi.withHandleUnchecked { handle ->
            return@withHandleUnchecked handle.createUpdate("DELETE FROM sample").execute()
        }
    }
}