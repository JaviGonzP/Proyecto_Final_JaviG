/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */


const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.deleteUser = functions.https.onCall(async (data, context) => {
  console.log("🟢 Valor de data.uid:", data.uid);

  const uid = data.uid;

  // 🟡 Log del UID recibido
  console.log("UID recibido para eliminar:", uid);

  if (!uid) {
    console.log("❌ UID no proporcionado.");
    return {
      success: false,
      message: "UID no proporcionado.",
    };
  }

  try {
    // 🔴 Eliminar de Authentication
    await admin.auth().deleteUser(uid);
    console.log(`✅ Usuario ${uid} eliminado de Authentication`);

    // 🔴 Eliminar de Firestore
    const usersRef = admin.firestore().collection("users");
    const snapshot = await usersRef.where("user_id", "==", uid).get();

    if (snapshot.empty) {
      console.log(`ℹ️ Usuario ${uid} no encontrado en Firestore`);
      return {
        success: true,
        message: `Usuario ${uid} eliminado de Auth.`,
      };
    }

    const deletePromises = snapshot.docs.map((doc) => doc.ref.delete());
    await Promise.all(deletePromises);

    console.log(`✅ Usuario ${uid} eliminado de Firestore`);

    return {
      success: true,
      message: `Usuario ${uid} eliminado correctamente de Auth y Firestore.`,
    };
  } catch (error) {
    console.error("❌ Error al eliminar usuario:", error);
    return {
      success: false,
      message: `Error al eliminar usuario: ${error.message}`,
    };
  }
});

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
