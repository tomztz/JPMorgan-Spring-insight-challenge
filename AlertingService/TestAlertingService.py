import pytest

from AlertingService import AlertingService

class TestAlertingService(object):

    @pytest.fixture
    def alert_service_instance(self):
        return AlertingService("alerts_rules.csv")

    def testAlertWithViolence(self, alert_service_instance):
        server_resource_info = "(1234,89,69,65)"
        result = alert_service_instance.generate_alert(server_resource_info)
        expected = "(Alert,1234, CPU_UTILIZATION VIOLATED, DISK_UTILIZATION VIOLATED)"
        assert result == expected

    def testNoAlert(self, alert_service_instance):
        server_resource_info = "(5678,80,50,40)"
        result = alert_service_instance.generate_alert(server_resource_info)
        expected = "(No Alert,5678)"
        assert result == expected

    if __name__ == '__main__':
        pytest.main(['-sv', 'AlertingService.py'])
