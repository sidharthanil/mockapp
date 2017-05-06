'use strict';

module.exports = function(Person) {

  Person.validatesUniquenessOf('phoneNumber',{message:'Phone number is already registered'});

};
