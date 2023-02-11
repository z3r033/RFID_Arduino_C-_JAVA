#include <SPI.h>
#include <MFRC522.h>
#define RST_PIN         9           // Configurable, see typical pin layout above
#define SS_PIN          10          // Configurable, see typical pin layout above
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.

MFRC522::MIFARE_Key key;
bool onetime;
void setup() {
   Serial.begin(9600);  // Initialize serial communications with the PC
    while (!Serial);    // Do nothing if no serial port is opened (added for Arduinos based on ATMEGA32U4)
    SPI.begin();    // Init SPI bus
    mfrc522.PCD_Init(); // Init MFRC522 card
    onetime =true;
    // Prepare the key (used both as key A)
    // using FFFFFFFFFFFFh which is the default at chip delivery from the factory
    for (byte i = 0; i < 6; i++) {
        key.keyByte[i] = 0xFF;
    }

    Serial.println(F("Scan a MIFARE Classic PICC to demonstrate Value Block mode."));
    Serial.print(F("Using key (for A):"));
    
    dump_byte_array(key.keyByte, MFRC522::MF_KEY_SIZE);
    Serial.println();
    
    Serial.println(F("BEWARE: Data will be written to the PICC, in sector #1"));

}

void loop() {
  // put your main code here, to run repeatedly:

  // Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
    if ( ! mfrc522.PICC_IsNewCardPresent())
        return;

    // Select one of the cards
    if ( ! mfrc522.PICC_ReadCardSerial())
        return;

    // Show some details of the PICC (that is: the tag/card)
    Serial.print(F("Card UID:"));
    
    dump_byte_array(mfrc522.uid.uidByte, mfrc522.uid.size);
    Serial.println();
    Serial.print(F("PICC type: "));
    MFRC522::PICC_Type piccType = mfrc522.PICC_GetType(mfrc522.uid.sak);
    String text2 = mfrc522.PICC_GetTypeName(piccType);
     Serial.println(mfrc522.PICC_GetTypeName(piccType));
    // Check for compatibility
    if (    piccType != MFRC522::PICC_TYPE_MIFARE_MINI
        &&  piccType != MFRC522::PICC_TYPE_MIFARE_1K
        &&  piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
        Serial.println(F("This sample only works with MIFARE Classic cards."));
        return;
    }

  

/* =========================================== */
if(onetime){
    byte sector         = 1;
    byte valueBlockA    = 4;
    byte trailerBlock   = 7;
    MFRC522::StatusCode status;
    byte buffer[18];
    byte size = sizeof(buffer);
    int32_t value;
     // Authenticate using key A
    Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("PCD_Authenticate() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    
 
    status = mfrc522.MIFARE_GetValue(valueBlockA, &value);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("mifare_GetValue() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    Serial.print("solde"); 
    Serial.print(valueBlockA);
    Serial.print("="); 
    Serial.print(value);
    Serial.println("=tx19"); 
    
      mfrc522.PCD_StopCrypto1();
       onetime=false;
  
}
//reding port
    String text = Serial.readString();
   
    int n = text.length();
    char char_array[n + 1];
    strcpy(char_array, text.c_str());
     Serial.write(char_array);
    int i = 0;
    char *p = strtok (char_array, ",");
    char *array[3];

    while (p != NULL)
    {
        array[i++] = p;
        p = strtok (NULL, ",");
    }
   // Serial.println(array[0]);
    String str=array[0];
    char str1[20];
    strcpy(str1, str.c_str());
   //  char str1[20]=str;
     char str2[20]="INC";
     char str3[20]="DEC";
     char str4[20]="LOG";
     char str5[20]="CHN";
   
if( strcmp(str1,str2)==0) { // if str2 = INC
    int incr = atoi( array[1]);
    Serial.print(incr);
     // In this sample we use the second sector,
    // that is: sector #1, covering block #4 up to and including block #7
    byte sector         = 1;
    byte valueBlockA    = 4;
   // byte valueBlockB   = 6;
    byte trailerBlock   = 7;
    MFRC522::StatusCode status;
    byte buffer[18];
    byte size = sizeof(buffer);
    int32_t value;
     // Authenticate using key A
    Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("PCD_Authenticate() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
        status = mfrc522.MIFARE_GetValue(valueBlockA, &value);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("mifare_GetValue() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    Serial.print("solde"); 
    Serial.print(valueBlockA);
    Serial.print("="); 
    Serial.print(value);
    Serial.println("=tx19"); 
    byte trailerBuffer[] = {
        255, 255, 255, 255, 255, 255,       // Keep default key A
        0, 0, 0,
        0,
        255, 255, 255, 255, 255, 255};      // Keep default key B
   
    mfrc522.MIFARE_SetAccessBits(&trailerBuffer[6], 0, 6, 6, 3);
 // Read the sector trailer as it is currently stored on the PICC
  //  Serial.println(F("Reading sector trailer..."));
    status = mfrc522.MIFARE_Read(trailerBlock, buffer, &size);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Read() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    // Check if it matches the desired access pattern already;
    // because if it does, we don't need to write it again...
    if (    buffer[6] != trailerBuffer[6]
        ||  buffer[7] != trailerBuffer[7]
        ||  buffer[8] != trailerBuffer[8]) {
        // They don't match (yet), so write it to the PICC
     //   Serial.println(F("Writing new sector trailer..."));
        status = mfrc522.MIFARE_Write(trailerBlock, trailerBuffer, 16);
        if (status != MFRC522::STATUS_OK) {
            Serial.print(F("MIFARE_Write() failed: "));
            Serial.println(mfrc522.GetStatusCodeName(status));
            return;
        }
    }
     // A value block has a 32 bit signed value stored three times
    // and an 8 bit address stored 4 times. Make sure that valueBlockA
    // and valueBlockB have that format (note that it will only format
    // the block when it doesn't comply to the expected format already).
    formatValueBlock(valueBlockA);
     // Add 1 to the value of valueBlockA and store the result in valueBlockA.
  //  Serial.print("Adding 1 to value of block "); Serial.println(valueBlockA);
    
    status = mfrc522.MIFARE_Increment(valueBlockA, incr);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Increment() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    status = mfrc522.MIFARE_Transfer(valueBlockA);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Transfer() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    // Show the new value of valueBlockA
    status = mfrc522.MIFARE_GetValue(valueBlockA, &value);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("mifare_GetValue() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    Serial.print("solde"); 
    Serial.print(valueBlockA);
    Serial.print("="); 
    Serial.print(value);
    Serial.println("=tx19"); 

    mfrc522.PCD_StopCrypto1();
     }else{
       mfrc522.PCD_StopCrypto1();
     }



   if( strcmp(str1,str3)==0) { // if str1 = DEC
    int decr = atoi( array[1]);
    Serial.print(decr);
     // In this sample we use the second sector,
    // that is: sector #1, covering block #4 up to and including block #7
    byte sector         = 1;
  
    byte valueBlockA    = 4;
   // byte valueBlockB   = 6;
    byte trailerBlock   = 7;
    MFRC522::StatusCode status;
    byte buffer[18];
    byte size = sizeof(buffer);
    int32_t value;
     // Authenticate using key A
    Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("PCD_Authenticate() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    
 
        status = mfrc522.MIFARE_GetValue(valueBlockA, &value);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("mifare_GetValue() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    Serial.print("solde"); 
    Serial.print(valueBlockA);
    Serial.print("="); 
    Serial.print(value);
    Serial.println("=tx19"); 

        // We need a sector trailer that defines blocks 5 and 6 as Value Blocks and enables key B
    // The last block in a sector (block #3 for Mifare Classic 1K) is the Sector Trailer.
    // See http://www.nxp.com/documents/data_sheet/MF1S503x.pdf sections 8.6 and 8.7:
    //      Bytes 0-5:   Key A
    //      Bytes 6-8:   Access Bits
    //      Bytes 9:     User data
    //      Bytes 10-15: Key B (or user data)
    byte trailerBuffer[] = {
        255, 255, 255, 255, 255, 255,       // Keep default key A
        0, 0, 0,
        0,
        255, 255, 255, 255, 255, 255};      // Keep default key B
    
    mfrc522.MIFARE_SetAccessBits(&trailerBuffer[6], 0, 6, 6, 3);
 // Read the sector trailer as it is currently stored on the PICC
  //  Serial.println(F("Reading sector trailer..."));
    status = mfrc522.MIFARE_Read(trailerBlock, buffer, &size);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Read() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    // Check if it matches the desired access pattern already;
    // because if it does, we don't need to write it again...
    if (    buffer[6] != trailerBuffer[6]
        ||  buffer[7] != trailerBuffer[7]
        ||  buffer[8] != trailerBuffer[8]) {
        // They don't match (yet), so write it to the PICC
     //   Serial.println(F("Writing new sector trailer..."));
        status = mfrc522.MIFARE_Write(trailerBlock, trailerBuffer, 16);
        if (status != MFRC522::STATUS_OK) {
            Serial.print(F("MIFARE_Write() failed: "));
            Serial.println(mfrc522.GetStatusCodeName(status));
            return;
        }
    }
    // A value block has a 32 bit signed value stored three times
    // and an 8 bit address stored 4 times. Make sure that valueBlockA
    // and valueBlockB have that format (note that it will only format
    // the block when it doesn't comply to the expected format already).
    formatValueBlock(valueBlockA);


    status = mfrc522.MIFARE_Decrement(valueBlockA, decr);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Decrement() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    status = mfrc522.MIFARE_Transfer(valueBlockA);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Transfer() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }
    // Show the new value of valueBlockB
    status = mfrc522.MIFARE_GetValue(valueBlockA, &value);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("mifare_GetValue() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }

    Serial.print("solde"); 
    Serial.print(valueBlockA);
    Serial.print("="); 
    Serial.print(value);
    Serial.println("=tx19"); 

    mfrc522.PCD_StopCrypto1();
    
    }else{
       mfrc522.PCD_StopCrypto1();
    }

   if(strcmp(str1,str4)==0) { //check si la premiere fois l'utilisation de la carte
    byte block;
    MFRC522::StatusCode status;
    byte buffer[34];
    String login = array[1];
    String pass= array[2];
    int n = login.length();
    char char_arr[n + 1];
    strcpy(buffer, login.c_str());
//check si la premiere fois l'utilisation de la carte
   byte len = 18;
  status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 14, &key, &(mfrc522.uid)); //line 834
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Authentication failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  byte buffer4[18];
  status = mfrc522.MIFARE_Read(14, buffer4, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Reading failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  Serial.write("0xchk//"); 
  for (uint8_t i = 0; i < 16; i++) {
    Serial.write(buffer4[i] );
  }
  Serial.write("//0xchk");
  char chk2[20]="on#";

//======================================= if 
if( strcmp(chk2,buffer4)==0) {     
  Serial.write("yes good");
  byte buffer2[18];
  byte len2 = 18;
  status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 12, &key, &(mfrc522.uid)); //line 834
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Authentication failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }

  status = mfrc522.MIFARE_Read(12, buffer2, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Reading failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  Serial.println("nl");
  Serial.print("0xlp//");
   char identifiant[16];
  for (uint8_t i = 0; i < 16; i++) {
    identifiant[i] = (char)buffer2[i];
   // Serial.write(buffer2[i] );
  }
   Serial.write(identifiant);
  
  Serial.println("//0xpl");

   status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, 13, &key, &(mfrc522.uid)); //line 834
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Authentication failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  byte buffer3[18];
  status = mfrc522.MIFARE_Read(13, buffer3, &len);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("Reading failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  Serial.println("nl");
  Serial.write("0xsp//");
   char pass[16];
  for (uint8_t i = 0; i < 16; i++) {
    //Serial.write(buffer3[i] );
      pass[i] = (char)buffer3[i];
  }
   
  Serial.write(pass);
  Serial.write("//0xps");
    String login = array[1];
    String pass2= array[2];
    Serial.println("testin");
if( (strcmp(identifiant,login.c_str())==0 )&& (strcmp(pass,pass2.c_str())==0)) { 
 Serial.write("0xsuccessx0");
  
}else{
  Serial.write("0xfailedx0");
}
Serial.println("fintesting");
  mfrc522.PCD_StopCrypto1();


}else{
  //Serial.write("Nop Nop");
     
    block = 12;
    //Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("PCD_Authenticate() success: "));

  // Write block
  status = mfrc522.MIFARE_Write(block, buffer, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Write() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("MIFARE_Write() success: "));

    int n2 = pass.length();
    char char_arr2[n2 + 1];
    strcpy(buffer, pass.c_str());
    block = 13;
    //Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("PCD_Authenticate() success: "));

  // Write block
  status = mfrc522.MIFARE_Write(block, buffer, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Write() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("MIFARE_Write() success: "));

    block =14  ;
    String chk="on#";
     strcpy(buffer, chk.c_str());
    //Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("PCD_Authenticate() success: "));

  // Write block
  status = mfrc522.MIFARE_Write(block, buffer, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Write() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("MIFARE_Write() success: "));

}
 // mfrc522.PCD_StopCrypto1();


  }else{
       mfrc522.PCD_StopCrypto1();
  }

  if( strcmp(str1,str5)==0) { //changer le mot de pass str1== CHN
    byte block;
    MFRC522::StatusCode status;
    byte buffer[34];
   // String login = array[1];
    String pass= array[1];
    byte len = 18;
     String user3="admin#";
    strcpy(buffer, user3.c_str());
    block = 12;
    //Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("PCD_Authenticate() success: "));

  // Write block
  status = mfrc522.MIFARE_Write(block, buffer, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Write() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("MIFARE_Write() success: "));
//changer le mot de pass str1== CHN
    int n2 = pass.length();
    char char_arr2[n2 + 1];
    strcpy(buffer, pass.c_str());
    block = 13;
    //Serial.println(F("Authenticating using key A..."));
    status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, block, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("PCD_Authenticate() success: "));

  // Write block
  status = mfrc522.MIFARE_Write(block, buffer, 16);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Write() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  else Serial.println(F("MIFARE_Write() success: "));

  }else{
       mfrc522.PCD_StopCrypto1();
  }

  
}






void dump_byte_array(byte *buffer, byte bufferSize) {
    for (byte i = 0; i < bufferSize; i++) {
        Serial.print(buffer[i] < 0x10 ? " 0" : " ");
        Serial.print(buffer[i], HEX);
    }
    
}



/**
 * Ensure that a given block is formatted as a Value Block.
 */
void formatValueBlock(byte blockAddr) {
    byte buffer[18];
    byte size = sizeof(buffer);
    MFRC522::StatusCode status;

    Serial.print(F("Reading block ")); Serial.println(blockAddr);
    status = mfrc522.MIFARE_Read(blockAddr, buffer, &size);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Read() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }

    if (    (buffer[0] == (byte)~buffer[4])
        &&  (buffer[1] == (byte)~buffer[5])
        &&  (buffer[2] == (byte)~buffer[6])
        &&  (buffer[3] == (byte)~buffer[7])

        &&  (buffer[0] == buffer[8])
        &&  (buffer[1] == buffer[9])
        &&  (buffer[2] == buffer[10])
        &&  (buffer[3] == buffer[11])

        &&  (buffer[12] == (byte)~buffer[13])
        &&  (buffer[12] ==        buffer[14])
        &&  (buffer[12] == (byte)~buffer[15])) {
    //    Serial.println(F("Block has correct Value Block format."));
    }
    else {
        Serial.println(F("Formatting as Value Block..."));
        byte valueBlock[] = {
            0, 0, 0, 0,
            255, 255, 255, 255,
            0, 0, 0, 0,
            blockAddr, ~blockAddr, blockAddr, ~blockAddr };
        status = mfrc522.MIFARE_Write(blockAddr, valueBlock, 16);
        if (status != MFRC522::STATUS_OK) {
            Serial.print(F("MIFARE_Write() failed: "));
            Serial.println(mfrc522.GetStatusCodeName(status));
        }
    }
}
