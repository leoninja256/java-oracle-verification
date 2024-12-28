package net.leoninja.javaoracleverification.sampleoraclesjis

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.statement.StatementException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class SampleRepositoryTest {
    private lateinit var jdbi: Jdbi

    @BeforeEach
    fun init() {
        jdbi = Jdbi.create("jdbc:oracle:thin:@localhost:1521/XEPDB1", "sample_user1", "sample_pass")
        jdbi.installPlugin(KotlinPlugin())
    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun test_create_桁数超過() {
        val sampleRepository = SampleRepository(jdbi)
        sampleRepository.deleteAll()

        try {
            sampleRepository.create(
                Sample(
                    id = "00002",
                    str1 = "あいうえ～",
                    str2 = "あいうえお～かきく", // 9文字
                )
            )
            fail("登録できてしまっている")
        } catch (ex: StatementException) {
            println(ex.message)
            assertTrue(ex.message!!.contains("ORA-12899:"))
        }

        try {
            sampleRepository.create(
                Sample(
                    id = "00003",
                    str1 = "ｱｲｳｴｵｶｷｸｹｺｻｼ",
                    str2 = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁ", // 17文字
                )
            )
            fail("登録できてしまっている")
        } catch (ex: StatementException) {
            println(ex.message)
            assertTrue(ex.message!!.contains("ORA-12899:"))
        }
    }

    @Test
    fun test_findAll() {
        val sampleRepository = SampleRepository(jdbi)
        sampleRepository.deleteAll()
        sampleRepository.create(
            Sample(
                id = "00001",
                str1 = "ABCDEFGHIJKL",
                str2 = "ABCDEFGHIJKLMNOP", // 16文字
            )
        )
        sampleRepository.create(
            Sample(
                id = "00002",
                str1 = "あいうえ～",
                str2 = "あいうえお～かき", // 8文字(Shift_JISなら2バイト)
            )
        )
        sampleRepository.create(
            Sample(
                id = "00003",
                str1 = "ｱｲｳｴｵｶｷｸｹｺｻｼ",
                str2 = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀ", // 16文字(Shift_JISなら1バイト)
            )
        )
        // 全て問題なくinsertできる

        val list = sampleRepository.findAll()
        list.forEach { println(it) }

        assertEquals(3, list.size)
        assertEquals("ABCDEFGHIJKLMNOP", list[0].str2)
        assertEquals("あいうえお～かき", list[1].str2)
        assertEquals("ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀ", list[2].str2)
    }
}