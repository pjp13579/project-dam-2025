// generateHash.js
import bcrypt from 'bcryptjs';

const plainPassword = 'string'; 
const saltRounds = 10;

bcrypt.hash(plainPassword, saltRounds, (err, hash) => {
  if (err) {
    console.error(err);
    return;
  }
  console.log('Hashed password:', hash);
  console.log('Use this hash in your MongoDB document');
});