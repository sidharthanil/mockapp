'use strict';

module.exports = function(Company) {
Company.validatesUniquenessOf('phoneNumber',{message:'Phone number is already registered'});
};
