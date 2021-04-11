import sys
import csv


class SanctionsScreening(object):
    """Utility Class to check if a payee is Sanction Hit/No with percentage."""

    def __init__(self, sanction_list_file, matching_percentage):
        self.sanction_list_file = sanction_list_file
        self.matching_percentage = matching_percentage

    def payee_sanction_screening(self, payee_name):
        """Check if payee is a Hit with matched percentage."""
        if (payee_name is None) or (len(payee_name) == 0):
            return 'No', 0.0
        matching_count = 0
        matching_percentage = 0.0
        length = len(payee_name)
        sanction_list = self.__read_sanction_list()
        for sanction_name in sanction_list:
            matching_count = sum(p == s for p, s in zip(payee_name, str(sanction_name[0])))
            matching_percentage = matching_count / length
            if matching_percentage >= self.matching_percentage:
                return "Hit", matching_percentage

        return 'No', matching_percentage

    def __read_sanction_list(self):
        sanction_list = []
        with open(self.sanction_list_file, "r") as list_file:
            rows = csv.reader(list_file)
            for row in rows:
                sanction_list.append(row)
            return sanction_list


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Please provide payee name.")
        sys.exit(1)

    the_payee_name = sys.argv[1]
    screening = SanctionsScreening("sanctions_list.csv", 0.75)
    result = screening.payee_sanction_screening(the_payee_name)
    print(result)
