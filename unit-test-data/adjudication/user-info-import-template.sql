DECLARE
    user_info_id character varying(255) := uuid_generate_v4();
    email_confirmation_id character varying(255) := uuid_generate_v4();
    adjudication_id character varying(255) := uuid_generate_v4();
    user_universal_id character varying(255) := 'CN=%CN%, OU=%CN%, O=%CN%, L=San Diego, ST=CA, C=US';
BEGIN
    insert into user_info (id, user_universal_id, sponsor_email, user_email, user_first_name, user_last_name, user_middle_initial, user_phone, user_email_confirmation) values
      (user_info_id, user_universal_id, '%SPONSOR_LAST%@localhost', '%LAST%@localhost', '%FIRST%', '%LAST%', 'B', '1234567890',  null)
    ;

    insert into email_confirmation (id, user_universal_id, confirmed_boolean) VALUES
      (email_confirmation_id, user_universal_id, true)
    ;

    update user_info set user_email_confirmation=email_confirmation_id where user_universal_id=user_universal_id;

    insert into adjudication (id, user_universal_id, adjudication_status) VALUES
      (adjudication_id, user_universal_id, 'CONFIRMED')
    ;


    insert into account_request (user_universal_id, adjudication_universal_id, userinfo_universal_id) VALUES
      (user_universal_id, user_universal_id, user_universal_id)
    ;
END
