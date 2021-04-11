import sys
import csv


class AlertingService(object):
    """Alert Service Class to generate alerts based on the server resource consumption and predefined alert rules ."""

    def __init__(self, alerts_rules_file):
        self.alerts_rules_file = alerts_rules_file

    def generate_alert(self, server_resource_parameters):
        """Generate alert if meet the alerting rules."""
        if (server_resource_parameters is None) or (len(server_resource_parameters) == 0):
            return "No Alert", "No Server Input"

        alert_rules = self.__read_alert_rules()
        server_resource_info = self.__parse_resource_parameters(server_resource_parameters)
        server_id = server_resource_info[0]
        server_resource_para = server_resource_info[1]

        violated_rules = []
        for para_key, para_value in server_resource_para.items():
            rule_value = alert_rules.get(para_key)
            if para_value > rule_value:
                violated_rules.append(para_key + ' VIOLATED')

        if violated_rules:
            return "(" + "Alert," + server_id + ", " + ", ".join(violated_rules) + ")"
        else:
            return "(" + "No Alert," + server_id + ")"

    def __read_alert_rules(self):
        alert_rules = {}
        with open(self.alerts_rules_file, "r") as rule_file:
            rules = csv.reader(rule_file)
            for rule in rules:
                alert_rules[rule[0]] = int(rule[1])
            return alert_rules

    def __parse_resource_parameters(self, server_resource_parameters):
        para_without_parenthesis = ''.join(c for c in server_resource_parameters if c not in '()')
        para_list = para_without_parenthesis.split(',')
        server_id = para_list[0]
        para_dic = {"CPU_UTILIZATION": int(para_list[1]), "MEMORY_UTILIZATION": int(para_list[2]),
                    "DISK_UTILIZATION": int(para_list[3])}

        return server_id, para_dic


if __name__ == '__main__':

    if len(sys.argv) < 2:
        print("Please provide server resource info")
        sys.exit(1)
    the_server_resource_info = sys.argv[1]

    # the_server_resource_info = "(1234,89,69,65)"
    alert_service = AlertingService("alerts_rules.csv")
    result = alert_service.generate_alert(the_server_resource_info)
    print(result)
