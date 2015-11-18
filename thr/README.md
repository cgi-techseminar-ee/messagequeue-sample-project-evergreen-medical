Introduction
------------

TODO: explain what this app does and how to run it.

Testing message publishers
--------------------------

To test workorder message publisher:

1. Add a new queue, `debug-queue` in RabbitMQ management environment
2. Add a binding from `workorder-exchange` to `debug-queue` (routing key is not required)
3. Login as admin/admin to THR admin environment at http://127.0.0.1:8080/#/login
3. Add a new workorder at http://127.0.0.1:8080/#/workorders
4. Examine `debug-queue`, it should now contain the workorder message

Testing message listeners
-------------------------

To test nurse message listener, publish the following message to
`nurse-exchange`:

    Routing key:
        nurse-queue
    Headers:
        __TypeId__: com.cgi.seminar.messaging.messages.NurseMessage
    Properties:
        content_encoding: UTF-8
        content_type: application/json
    Payload:
        {"id": 1, "name": "Jane Nurse"}

You should see the following in debug log (`insert` will be missing if you have
an employee with external ID 1 already):


    [INFO] com.cgi.seminar.messaging.consumers.NurseMessageListener - Received Nurse message: NurseMessage{id=1, name='Jane Nurse'}
    Hibernate: select employee0_.id as id1_0_, employee0_.external_id as external2_0_, employee0_.name as name3_0_ from employee employee0_ where employee0_.external_id=?
    Hibernate: insert into employee (id, external_id, name) values (null, ?, ?)

and a new employee, *Jane Nurse* should be visible in employee entity list.

To test home address message listener, publish the following message to
`homeaddress-exchange`:

    Routing key:
        homeaddress-queue
    Headers:
        __TypeId__: com.cgi.seminar.messaging.messages.HomeAddressMessage
    Properties:
        content_encoding: UTF-8
        content_type: application/json
    Payload:
        {"id": 1, "name": "Home Address 1", "latitude": 0, "longitude": 0,
         "address": "Homely City, Cosy Street 2"}
