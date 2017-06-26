insert into Zone values("9ff78a6a-2216-4f38-bfeb-5fa189b6421b", "bathroom");
insert into Zone values("743d2365-2eaa-412f-8324-6b6b1361ba5b", "kitchen");
insert into Zone values("183f0204-5029-4b33-a128-404ba5c68fa8", "bedroom");

insert into Position values("eb264eea-4106-46a3-9992-70f16f283a15", "0", "0", "0", "9ff78a6a-2216-4f38-bfeb-5fa189b6421b");
insert into Position values("5f484241-6dcc-4731-846c-7fc3e4f0fafb", "1", "1", "1", "743d2365-2eaa-412f-8324-6b6b1361ba5b");
insert into Position values("c36e7f61-ba7b-408f-b113-c528980e7131", "2", "2", "2", "183f0204-5029-4b33-a128-404ba5c68fa8");

insert into Measurement values("59d46ae9-e0c8-48d0-b14a-503ed414b7cc", "2015-07-10 15:00:00", "0", "0", "0", "0", "00", "00", "00", "eb264eea-4106-46a3-9992-70f16f283a15");
insert into Measurement values("4304fe32-8028-4830-bebc-dd1d535e5cfd", "2015-07-11 20:00:00", "1", "1", "1", "1", "11", "11", "11", "5f484241-6dcc-4731-846c-7fc3e4f0fafb");
insert into Measurement values("43c6270b-2d12-49f3-ac8f-0d09d55fcf61", "2015-07-12 12:00:00", "2", "2", "2", "2", "22", "22", "22", "c36e7f61-ba7b-408f-b113-c528980e7131");

--insert into RFIDTags values(unhex("15F4B3D1"), "59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
--insert into RFIDTags values(unhex("15F4B3D2"), "4304fe32-8028-4830-bebc-dd1d535e5cfd");
--insert into RFIDTags values(unhex("15F4B3D3"), "43c6270b-2d12-49f3-ac8f-0d09d55fcf61");

insert into WIFIRSSI values("SSID1", -23, "59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
insert into WIFIRSSI values("SSID2", -30, "4304fe32-8028-4830-bebc-dd1d535e5cfd");
insert into WIFIRSSI values("SSID4", -30, "4304fe32-8028-4830-bebc-dd1d535e5cfd");
insert into WIFIRSSI values("SSID3", -10, "43c6270b-2d12-49f3-ac8f-0d09d55fcf61");

insert into BluetoothTags values("B1", "59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
insert into BluetoothTags values("B2", "4304fe32-8028-4830-bebc-dd1d535e5cfd");
insert into BluetoothTags values("B4", "4304fe32-8028-4830-bebc-dd1d535e5cfd");
insert into BluetoothTags values("B3", "43c6270b-2d12-49f3-ac8f-0d09d55fcf61");