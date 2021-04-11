import pytest

from SanctionsScreening import SanctionsScreening


class TestSanctionScreening(object):

    @pytest.fixture
    def screening_instance(self):
        return SanctionsScreening("sanctions_list.csv", 0.75)

    def testHitWithPercentage1(self, screening_instance):
        the_payee_name = "Kristopher Toe"
        result = screening_instance.payee_sanction_screening(the_payee_name)
        expected = ('Hit', 0.9285714285714286)
        assert result == expected

    def testHitWithPercentage2(self, screening_instance):
        the_payee_name = "Kristophre Doe"
        result = screening_instance.payee_sanction_screening(the_payee_name)
        expected = ('Hit', 0.8571428571428571)
        assert result == expected

    def testHitWithPercentage3(self, screening_instance):
        the_payee_name = "Kristopher Doe"
        result = screening_instance.payee_sanction_screening(the_payee_name)
        expected = ('Hit', 1.0)
        assert result == expected

    def testNoHitWithPercentage(self, screening_instance):
        the_payee_name = "Tianze Zhang"
        result = screening_instance.payee_sanction_screening(the_payee_name)
        expected = ('No', 0.0)
        assert result == expected

    if __name__ == '__main__':
        pytest.main(['-sv', 'SanctionScreening.py'])
