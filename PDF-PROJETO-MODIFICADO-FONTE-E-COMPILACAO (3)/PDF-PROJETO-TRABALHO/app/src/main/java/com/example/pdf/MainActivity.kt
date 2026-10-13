package com.example.pdf

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.content.Context
import android.content.ClipData
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.media.Ringtone
import android.media.RingtoneManager
import android.view.Surface
import android.view.TextureView
import android.view.Gravity
import android.widget.FrameLayout
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Image as ImageIcon
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import android.net.Uri
import android.provider.MediaStore
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerator
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.audio.JavaAudioDeviceModule
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpTransceiver
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

private val Bg = Color(0xFF090B0F)
private val SurfaceDark = Color(0xFF12161C)
private val Surface2 = Color(0xFF1A2028)
private val Accent = Color(0xFF25D4B0)
private val TextMain = Color(0xFFF3F5F7)
private val TextMuted = Color(0xFF98A1AC)
private val BubbleMine = Color(0xFF1D5F52)
private val BubbleOther = Color(0xFF1A2028)

private data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val blocked: Boolean = false,
    val avatarData: String? = null,
    val online: Boolean = false,
    val lastSeen: Long? = null
)

private data class ChatMessage(
    val id: Long,
    val contactId: String,
    val text: String,
    val mine: Boolean,
    val time: String,
    val attachment: String? = null,
    val remoteId: String? = null,
    val createdAt: Long = id,
    val deliveredAt: Long? = null,
    val readAt: Long? = null
)

private data class PendingMedia(
    val contactId: String,
    val names: List<String>
) {
    constructor(contactId: String, name: String) : this(contactId, listOf(name))
}

private data class PendingLocation(
    val contactId: String,
    val fileName: String
)

private enum class CallType { VOICE, VIDEO }

private data class IncomingCallInfo(
    val callId: String,
    val callerUid: String,
    val type: CallType,
    val callerName: String,
    val offerSdp: String
)

private data class ActiveCallInfo(
    val callId: String,
    val contactName: String,
    val type: CallType,
    val incoming: Boolean
)

private class PdfCallManager(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val onConnected: () -> Unit,
    private val onDisconnected: () -> Unit,
    private val onRemoteVideo: (VideoTrack) -> Unit,
    private val onCallFinished: () -> Unit,
    private val onError: (String) -> Unit
) {

    private val appContext = context.applicationContext
    private val db = firestore
    private val audioManager: AudioManager by lazy {
        appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun configureCallAudio(type: CallType) {
        runCatching {
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            // A chamada de vídeo usa o alto-falante; voz mantém a rota normal.
            audioManager.isSpeakerphoneOn = (type == CallType.VIDEO)
        }
    }
    private var factory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var audioSource: AudioSource? = null
    private var audioTrack: AudioTrack? = null
    private var videoSource: VideoSource? = null
    private var videoTrack: VideoTrack? = null
    private var videoCapturer: VideoCapturer? = null
    private var eglBase: EglBase? = null
    private var surfaceTextureHelper: org.webrtc.SurfaceTextureHelper? = null
    private var localRenderer: SurfaceViewRenderer? = null
    private var remoteRenderer: SurfaceViewRenderer? = null
    private var currentCallId: String? = null
    private var currentRemoteUid: String? = null
    private var currentType: CallType = CallType.VOICE
    private var currentIncoming = false
    private var candidateListener: ListenerRegistration? = null
    private var callListener: ListenerRegistration? = null
    private val pendingRemoteCandidates = mutableListOf<IceCandidate>()
    private var muted = false
    private var cameraEnabled = true
    private var usingFrontCamera = true

    private val initialized: Boolean
        get() = factory != null

    fun attachLocalRenderer(renderer: SurfaceViewRenderer?) {
        localRenderer = renderer
        videoTrack?.let { track ->
            renderer?.let { track.addSink(it) }
        }
    }

    fun attachRemoteRenderer(renderer: SurfaceViewRenderer?) {
        remoteRenderer = renderer
        renderer?.let { r ->
            lastRemoteTrack?.addSink(r)
        }
    }

    private var lastRemoteTrack: VideoTrack? = null

    fun startOutgoing(targetUid: String, type: CallType, callerName: String = "Usuário PDF"): String? {
        val callerUid = auth.currentUser?.uid
        val cleanTarget = targetUid.trim()
        if (callerUid.isNullOrBlank() || cleanTarget.isBlank()) {
            onError("Contato inválido.")
            return null
        }
        if (callerUid == cleanTarget) {
            onError("Você não pode ligar para o próprio aparelho.")
            return null
        }

        currentType = type
        currentRemoteUid = cleanTarget
        currentIncoming = false
        configureCallAudio(type)
        if (!ensureFactory()) return null
        createPeerConnection()
        if (peerConnection == null) { onError("Não foi possível criar a conexão da chamada."); return null }
        createLocalTracks(type == CallType.VIDEO)
        configureCallAudio(type)

        val callRef = db.collection("calls").document()
        currentCallId = callRef.id

        val data = hashMapOf<String, Any>(
            "callerUid" to callerUid,
            "calleeUid" to cleanTarget,
            "type" to type.name,
            "status" to "ringing",
            "callerName" to callerName,
            "createdAt" to System.currentTimeMillis()
        )

        callRef.set(data)
            .addOnSuccessListener {
                createOffer(callRef)
                listenForAnswer(callRef)
                listenForCandidates(callRef, callerUid, cleanTarget)
            }
            .addOnFailureListener {
                onError("Não foi possível iniciar a chamada.")
                cleanup(false)
            }

        return callRef.id
    }

    fun acceptIncoming(info: IncomingCallInfo) {
        val myUid = auth.currentUser?.uid
        if (myUid.isNullOrBlank()) {
            onError("O PDF ainda está conectando.")
            return
        }

        currentCallId = info.callId
        currentRemoteUid = info.callerUid
        currentType = info.type
        currentIncoming = true
        configureCallAudio(info.type)

        if (!ensureFactory()) return
        createPeerConnection()
        if (peerConnection == null) { onError("Não foi possível criar a conexão da chamada."); return }
        createLocalTracks(info.type == CallType.VIDEO)
        configureCallAudio(info.type)

        val callRef = db.collection("calls").document(info.callId)
        val offer = SessionDescription(SessionDescription.Type.OFFER, info.offerSdp)

        peerConnection?.setRemoteDescription(object : SimpleSdpObserver() {
            override fun onSetSuccess() {
                drainPendingRemoteCandidates()
                createAnswer(callRef)
            }

            override fun onSetFailure(error: String?) {
                onError("Não foi possível aceitar a chamada.")
                cleanup(false)
            }
        }, offer)

        listenForCandidates(callRef, info.callerUid, myUid)
    }

    fun rejectIncoming(info: IncomingCallInfo) {
        db.collection("calls").document(info.callId)
            .update("status", "rejected")
            .addOnCompleteListener { cleanup(false) }
    }

    fun endCall() {
        val callId = currentCallId
        if (!callId.isNullOrBlank()) {
            db.collection("calls").document(callId)
                .update("status", "ended")
                .addOnCompleteListener { cleanup(true) }
        } else {
            cleanup(true)
        }
    }

    fun observeCallEnd(callId: String) {
        callListener?.remove()
        callListener = db.collection("calls").document(callId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
                val status = snapshot.getString("status") ?: return@addSnapshotListener
                if (status == "ended" || status == "rejected" || status == "busy") {
                    cleanup(false)
                }
            }
    }

    fun toggleMute(): Boolean {
        muted = !muted
        audioTrack?.setEnabled(!muted)
        return muted
    }

    fun toggleCamera(): Boolean {
        cameraEnabled = !cameraEnabled
        videoTrack?.setEnabled(cameraEnabled)
        return cameraEnabled
    }

    fun switchCamera() {
        val capturer = videoCapturer
        if (capturer is org.webrtc.CameraVideoCapturer) {
            capturer.switchCamera(null)
            usingFrontCamera = !usingFrontCamera
        }
    }

    fun eglContext(): EglBase.Context? = eglBase?.eglBaseContext

    fun release() {
        cleanup(false)
        runCatching { factory?.dispose() }
        factory = null
        runCatching { eglBase?.release() }
        eglBase = null
    }

    private fun ensureFactory(): Boolean {
        if (initialized) return true
        val result = runCatching {
            PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions
                    .builder(appContext)
                    .createInitializationOptions()
            )

            eglBase = EglBase.create()
            val encoderFactory = DefaultVideoEncoderFactory(
                eglBase!!.eglBaseContext,
                true,
                true
            )
            val decoderFactory = DefaultVideoDecoderFactory(eglBase!!.eglBaseContext)
            val audioDeviceModule = JavaAudioDeviceModule
                .builder(appContext)
                .createAudioDeviceModule()

            factory = PeerConnectionFactory.builder()
                .setAudioDeviceModule(audioDeviceModule)
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory()
        }
        if (result.isFailure || !initialized) {
            onError("Não foi possível preparar a chamada. Verifique a conexão e tente novamente.")
            return false
        }
        return true
    }

    private fun createPeerConnection() {
        peerConnection?.close()
        peerConnection = null

        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun.cloudflare.com:3478").createIceServer()
        )

        peerConnection = factory?.createPeerConnection(
            iceServers,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate) {
                    sendIceCandidate(candidate)
                }

                override fun onIceCandidatesRemoved(candidates: Array<IceCandidate>) {}
                override fun onSignalingChange(state: PeerConnection.SignalingState) {}
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
                    when (state) {
                        PeerConnection.IceConnectionState.CONNECTED,
                        PeerConnection.IceConnectionState.COMPLETED -> {
                            configureCallAudio(currentType)
                            onConnected()
                        }
                        PeerConnection.IceConnectionState.FAILED,
                        PeerConnection.IceConnectionState.DISCONNECTED -> onDisconnected()
                        else -> Unit
                    }
                }
                override fun onIceConnectionReceivingChange(receiving: Boolean) {}
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {}
                override fun onAddStream(stream: org.webrtc.MediaStream) {
                    stream.videoTracks.firstOrNull()?.let { track ->
                        lastRemoteTrack = track
                        onRemoteVideo(track)
                        remoteRenderer?.let { track.addSink(it) }
                    }
                }
                override fun onRemoveStream(stream: org.webrtc.MediaStream) {}
                override fun onDataChannel(dataChannel: org.webrtc.DataChannel) {}
                override fun onRenegotiationNeeded() {}
                override fun onAddTrack(receiver: org.webrtc.RtpReceiver, mediaStreams: Array<org.webrtc.MediaStream>) {
                    val track = receiver.track()
                    if (track is VideoTrack) {
                        lastRemoteTrack = track
                        onRemoteVideo(track)
                        remoteRenderer?.let { track.addSink(it) }
                    }
                }
                override fun onTrack(transceiver: RtpTransceiver) {
                    val track = transceiver.receiver.track()
                    if (track is VideoTrack) {
                        lastRemoteTrack = track
                        onRemoteVideo(track)
                        remoteRenderer?.let { track.addSink(it) }
                    }
                }
                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {
                    when (newState) {
                        PeerConnection.PeerConnectionState.CONNECTED -> {
                            configureCallAudio(currentType)
                            onConnected()
                        }
                        PeerConnection.PeerConnectionState.FAILED,
                        PeerConnection.PeerConnectionState.DISCONNECTED,
                        PeerConnection.PeerConnectionState.CLOSED -> onDisconnected()
                        else -> Unit
                    }
                }
                override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState) {}
            }
        )
    }

    private fun createLocalTracks(withVideo: Boolean) {
        val f = factory ?: return
        val pc = peerConnection ?: return

        val audioConstraints = MediaConstraints()
        audioSource = f.createAudioSource(audioConstraints)
        audioTrack = f.createAudioTrack("PDF_AUDIO", audioSource)
        audioTrack?.setEnabled(true)
        audioTrack?.let { pc.addTrack(it, listOf("PDF_STREAM")) }

        if (!withVideo) return

        val capturer = createCameraCapturer()
        if (capturer == null) {
            onError("Não foi possível abrir a câmera.")
            return
        }
        videoCapturer = capturer
        val source = f.createVideoSource(capturer.isScreencast)
        videoSource = source
        val helper = SurfaceTextureHelper.create("PDF_CAMERA_THREAD", eglBase!!.eglBaseContext)
        surfaceTextureHelper = helper
        capturer.initialize(helper, appContext, source.capturerObserver)
        videoTrack = f.createVideoTrack("PDF_VIDEO", source)
        videoTrack?.setEnabled(true)
        videoTrack?.let { track ->
            localRenderer?.let { track.addSink(it) }
            pc.addTrack(track, listOf("PDF_STREAM"))
        }
        runCatching { capturer.startCapture(640, 480, 24) }
    }

    private fun createCameraCapturer(): VideoCapturer? {
        val enumerator: CameraEnumerator = if (Camera2Enumerator.isSupported(appContext)) {
            Camera2Enumerator(appContext)
        } else {
            Camera1Enumerator(false)
        }

        enumerator.deviceNames.firstOrNull { enumerator.isFrontFacing(it) }?.let {
            enumerator.createCapturer(it, null)?.let { capturer -> return capturer }
        }

        enumerator.deviceNames.firstOrNull()?.let {
            return enumerator.createCapturer(it, null)
        }
        return null
    }

    private fun createOffer(callRef: com.google.firebase.firestore.DocumentReference) {
        peerConnection?.createOffer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(description: SessionDescription) {
                peerConnection?.setLocalDescription(object : SimpleSdpObserver() {
                    override fun onSetSuccess() {
                        callRef.update("offerSdp", description.description)
                    }
                    override fun onSetFailure(error: String?) {
                        onError("Não foi possível preparar a chamada.")
                    }
                }, description)
            }
            override fun onCreateFailure(error: String?) {
                onError("Não foi possível criar a chamada.")
            }
        }, MediaConstraints())
    }

    private fun createAnswer(callRef: com.google.firebase.firestore.DocumentReference) {
        peerConnection?.createAnswer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(description: SessionDescription) {
                peerConnection?.setLocalDescription(object : SimpleSdpObserver() {
                    override fun onSetSuccess() {
                        callRef.update(
                            mapOf(
                                "answerSdp" to description.description,
                                "status" to "accepted"
                            )
                        )
                        observeCallEnd(callRef.id)
                    }
                    override fun onSetFailure(error: String?) {
                        onError("Não foi possível responder à chamada.")
                    }
                }, description)
            }
            override fun onCreateFailure(error: String?) {
                onError("Não foi possível responder à chamada.")
            }
        }, MediaConstraints())
    }

    private fun listenForAnswer(callRef: com.google.firebase.firestore.DocumentReference) {
        callListener?.remove()
        callListener = callRef.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
            val answer = snapshot.getString("answerSdp")
            if (!answer.isNullOrBlank() && peerConnection?.remoteDescription == null) {
                peerConnection?.setRemoteDescription(
                    object : SimpleSdpObserver() {
                        override fun onSetSuccess() {
                            drainPendingRemoteCandidates()
                        }
                    },
                    SessionDescription(SessionDescription.Type.ANSWER, answer)
                )
            }
            val status = snapshot.getString("status")
            if (status == "ended" || status == "rejected") cleanup(false)
        }
    }

    private fun listenForCandidates(
        callRef: com.google.firebase.firestore.DocumentReference,
        remoteUid: String,
        myUid: String
    ) {
        candidateListener?.remove()
        val collectionName = if (currentIncoming) "callerCandidates" else "calleeCandidates"
        candidateListener = callRef.collection(collectionName)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                for (doc in snapshot.documentChanges) {
                    if (doc.type != com.google.firebase.firestore.DocumentChange.Type.ADDED) continue
                    val data = doc.document.data
                    val sdp = data["sdp"] as? String ?: continue
                    val sdpMid = data["sdpMid"] as? String
                    val line = (data["sdpMLineIndex"] as? Number)?.toInt() ?: 0
                    val candidate = IceCandidate(sdpMid, line, sdp)
                    if (peerConnection?.remoteDescription == null) {
                        pendingRemoteCandidates.add(candidate)
                    } else {
                        runCatching { peerConnection?.addIceCandidate(candidate) }
                    }
                }
            }
    }

    private fun drainPendingRemoteCandidates() {
        if (pendingRemoteCandidates.isEmpty()) return
        val pc = peerConnection ?: return
        pendingRemoteCandidates.toList().forEach { candidate ->
            runCatching { pc.addIceCandidate(candidate) }
        }
        pendingRemoteCandidates.clear()
    }

    private fun sendIceCandidate(candidate: IceCandidate) {
        val callId = currentCallId ?: return
        val callerUid = auth.currentUser?.uid ?: return
        val collectionName = if (currentIncoming) "calleeCandidates" else "callerCandidates"
        db.collection("calls").document(callId)
            .collection(collectionName)
            .add(
                mapOf(
                    "sdp" to candidate.sdp,
                    "sdpMid" to candidate.sdpMid,
                    "sdpMLineIndex" to candidate.sdpMLineIndex,
                    "fromUid" to callerUid
                )
            )
    }

    private fun cleanup(deleteDoc: Boolean) {
        val callId = currentCallId
        candidateListener?.remove()
        candidateListener = null
        callListener?.remove()
        callListener = null

        runCatching { videoCapturer?.stopCapture() }
        runCatching { videoCapturer?.dispose() }
        videoCapturer = null
        runCatching { surfaceTextureHelper?.dispose() }
        surfaceTextureHelper = null
        videoTrack?.dispose()
        videoTrack = null
        videoSource?.dispose()
        videoSource = null
        audioTrack?.dispose()
        audioTrack = null
        audioSource?.dispose()
        audioSource = null
        lastRemoteTrack = null
        pendingRemoteCandidates.clear()
        peerConnection?.close()
        peerConnection = null
        currentCallId = null
        currentRemoteUid = null
        muted = false
        cameraEnabled = true
        runCatching {
            audioManager.isSpeakerphoneOn = false
            audioManager.mode = AudioManager.MODE_NORMAL
        }

        if (!callId.isNullOrBlank()) {
            onCallFinished()
        }

        if (deleteDoc && !callId.isNullOrBlank()) {
            db.collection("calls").document(callId).delete()
        }
    }
}

private open class SimpleSdpObserver : SdpObserver {
    override fun onCreateSuccess(description: SessionDescription) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(error: String?) {}
    override fun onSetFailure(error: String?) {}
}

class MainActivity : ComponentActivity() {

    private val prefs by lazy {
        getSharedPreferences("pdf_private", Context.MODE_PRIVATE)
    }

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private var callManager: PdfCallManager? = null
    private var incomingCall by mutableStateOf<IncomingCallInfo?>(null)
    private var activeCall by mutableStateOf<ActiveCallInfo?>(null)
    private var callConnected by mutableStateOf(false)
    private var callMuted by mutableStateOf(false)
    private var callCameraEnabled by mutableStateOf(true)
    private var incomingCallListener: ListenerRegistration? = null
    @Volatile private var activityResumed = false
    @Volatile private var chatScreenVisible = false
    private val mediaWorkScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var incomingRingtone: Ringtone? = null
    private val downloadingAttachmentIds = mutableSetOf<String>()
    private val attachmentReadyCallbacks = mutableMapOf<String, MutableList<(String) -> Unit>>()
    private var messagesListener: ListenerRegistration? = null
    private var globalConversationsListener: ListenerRegistration? = null
    private val globalMessageListeners = mutableMapOf<String, ListenerRegistration>()
    private var profileAvatarFileState by mutableStateOf<String?>(null)

    private var unlocked by mutableStateOf(false)

    private var ignoreNextStop = false

    private var pendingMedia by mutableStateOf<PendingMedia?>(null)

    // Seleciona vários arquivos de mídia de uma só vez e copia cada um para filesDir,
    // que é privado do aplicativo e não aparece automaticamente na galeria.
    private val mediaPicker =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            mediaWorkScope.launch {
                val copiedNames = withContext(Dispatchers.IO) {
                    uris.mapNotNull { uri -> copyMediaToPrivateStorage(uri) }
                }
                if (copiedNames.isNotEmpty()) {
                    pendingMedia = PendingMedia(contactId = mediaContactId, names = copiedNames)
                }
                ignoreNextStop = false
            }
        }

    private val profileGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                val copied = copyProfileImageToPrivateStorage(uri)
                if (!copied.isNullOrBlank()) {
                    prefs.edit().putString("profile_avatar_file", copied).commit()
                    profileAvatarFileState = copied
                } else {
                    android.widget.Toast.makeText(
                        this,
                        "Não foi possível carregar essa foto. Tente escolher outra.",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }
            ignoreNextStop = false
        }

    private val profileCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            val file = profileCameraOutputFile
                ?: prefs.getString("profile_camera_pending_file", null)?.let { File(filesDir, "private_media/$it") }
            if (success && file != null && file.exists() && file.length() > 0L) {
                prefs.edit().putString("profile_avatar_file", file.name).remove("profile_camera_pending_file").commit()
                profileAvatarFileState = file.name
            } else {
                file?.delete()
                prefs.edit().remove("profile_camera_pending_file").apply()
                android.widget.Toast.makeText(
                    this,
                    "A câmera não retornou a foto. Tente novamente ou escolha uma foto da galeria.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
            profileCameraOutputFile = null
            ignoreNextStop = false
        }

    private var profileCameraOutputFile: File? = null

    private val documentPicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                val copiedName = copyDocumentToPrivateStorage(uri)
                if (copiedName != null) {
                    pendingMedia = PendingMedia(
                        contactId = documentContactId,
                        name = copiedName
                    )
                }
            }
            ignoreNextStop = false
        }

    private var mediaContactId: String = ""

    private var documentContactId: String = ""

    private var locationContactId: String = ""

    private var pendingLocation by mutableStateOf<PendingLocation?>(null)

    private var cameraContactId: String = ""
    private var cameraOutputFile: File? = null
    private var pendingCameraAction: String? = null

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val action = pendingCameraAction
            pendingCameraAction = null

            if (!granted) {
                cameraOutputFile?.delete()
                cameraOutputFile = null
                ignoreNextStop = false
                return@registerForActivityResult
            }

            when (action) {
                "photo" -> launchPreparedPhoto()
                "video" -> launchPreparedVideo()
            }
        }

    private val cameraPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            val file = cameraOutputFile
            if (success && file != null && file.exists() && file.length() > 0L) {
                pendingMedia = PendingMedia(
                    contactId = cameraContactId,
                    name = file.name
                )
            } else {
                file?.delete()
            }
            cameraOutputFile = null
            ignoreNextStop = false
        }

    private val cameraVideoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val file = cameraOutputFile
            val success = result.resultCode == RESULT_OK && file != null && file.exists() && file.length() > 0L

            if (success && file != null) {
                pendingMedia = PendingMedia(
                    contactId = cameraContactId,
                    name = file.name
                )
            } else {
                file?.delete()
                if (result.resultCode == RESULT_OK) {
                    android.widget.Toast.makeText(this, "A câmera não salvou o vídeo. Tente gravar novamente.", android.widget.Toast.LENGTH_LONG).show()
                }
            }

            cameraOutputFile = null
            ignoreNextStop = false
        }

    private var recorder: MediaRecorder? = null
    private var recordingFile: File? = null
    private var recordingContactId: String = ""
    private var isRecording by mutableStateOf(false)

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val granted = result[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                createLocationAttachment(locationContactId)
            }

            ignoreNextStop = false
        }

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startAudioRecording(recordingContactId)
            } else {
                isRecording = false
            }
        }

    // Permissões obrigatórias para iniciar ou atender chamadas.
    private var pendingCallAction: (() -> Unit)? = null
    private val callPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val action = pendingCallAction
            pendingCallAction = null
            ignoreNextStop = false
            if (result.values.all { it }) action?.invoke() else {
                android.widget.Toast.makeText(this, "Permita o microfone e, para vídeo, a câmera para usar chamadas no PDF.", android.widget.Toast.LENGTH_LONG).show()
            }
        }

    private fun requestCallPermissions(type: CallType, action: () -> Unit) {
        val required = mutableListOf(android.Manifest.permission.RECORD_AUDIO)
        if (type == CallType.VIDEO) required.add(android.Manifest.permission.CAMERA)
        val missing = required.filter { checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
        if (missing.isEmpty()) action() else {
            pendingCallAction = action
            // Evita que o bloqueio automático do PDF encerre a Activity enquanto a permissão está aberta.
            ignoreNextStop = true
            callPermissionLauncher.launch(missing.toTypedArray())
        }
    }

    private fun startAudioRecording(contactId: String) {
        if (Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            recordingContactId = contactId
            audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            return
        }

        if (isRecording) return

        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val file = File(dir, "audio_${System.currentTimeMillis()}.m4a")

        try {
            val r = MediaRecorder()
            r.setAudioSource(MediaRecorder.AudioSource.MIC)
            r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            r.setAudioSamplingRate(44100)
            r.setAudioEncodingBitRate(128000)
            r.setOutputFile(file.absolutePath)
            r.prepare()
            r.start()
            recorder = r
            recordingFile = file
            recordingContactId = contactId
            isRecording = true
        } catch (_: Exception) {
            runCatching { recorder?.release() }
            recorder = null
            file.delete()
            isRecording = false
        }
    }

    private fun stopAudioRecording(): PendingMedia? {
        if (!isRecording) return null
        val file = recordingFile
        val contactId = recordingContactId
        return try {
            recorder?.stop()
            recorder?.release()
            recorder = null
            recordingFile = null
            isRecording = false
            if (file != null && file.exists() && file.length() > 0L) {
                PendingMedia(contactId, file.name)
            } else null
        } catch (_: Exception) {
            runCatching { recorder?.release() }
            recorder = null
            recordingFile?.delete()
            recordingFile = null
            isRecording = false
            null
        }
    }

    private fun conversationId(uid1: String, uid2: String): String {
        val a = minOf(uid1, uid2)
        val b = maxOf(uid1, uid2)
        return hash("pdf-conversation:$a:$b").take(48)
    }

    private fun sendRemoteText(contact: Contact, text: String, clientMessageId: String? = null, onDone: (Boolean) -> Unit = {}) {
        val myUid = auth.currentUser?.uid
        if (myUid.isNullOrBlank()) { onDone(false); return }
        resolvePhoneToUid(contact.number, { targetUid ->
            if (targetUid == myUid) { onDone(false); return@resolvePhoneToUid }
            val now = System.currentTimeMillis()
            val conversation = firestore.collection("conversations").document(conversationId(myUid, targetUid))
            val message = mapOf(
                "senderUid" to myUid,
                "recipientUid" to targetUid,
                "text" to text,
                "createdAt" to now,
                "deliveredAt" to null,
                "readAt" to null
            )
            val messageRef = if (clientMessageId.isNullOrBlank()) conversation.collection("messages").document() else conversation.collection("messages").document(clientMessageId)
            conversation.set(mapOf("participants" to listOf(myUid, targetUid), "updatedAt" to now), SetOptions.merge())
                // Só tenta gravar a mensagem depois que a conversa foi criada/atualizada com sucesso.
                .onSuccessTask { messageRef.set(message) }
                .addOnSuccessListener { onDone(true) }
                .addOnFailureListener { error ->
                    val detail = error.message?.takeIf { it.isNotBlank() } ?: "erro desconhecido"
                    android.util.Log.e("PDF-MENSAGENS", "Falha ao enviar mensagem", error)
                    android.widget.Toast.makeText(
                        this,
                        "Falha ao enviar: $detail",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                    onDone(false)
                }
        }, { onDone(false); android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show() })
    }

    private fun stopGlobalMessageSync() {
        globalConversationsListener?.remove()
        globalConversationsListener = null
        globalMessageListeners.values.forEach { it.remove() }
        globalMessageListeners.clear()
    }

    private fun startGlobalMessageSync(onBatch: (Contact, List<ChatMessage>) -> Unit) {
        stopGlobalMessageSync()
        val myUid = auth.currentUser?.uid ?: return
        globalConversationsListener = firestore.collection("conversations")
            .whereArrayContains("participants", myUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val activeIds = snapshot.documents.map { it.id }.toSet()
                globalMessageListeners.keys.filter { it !in activeIds }.forEach { id ->
                    globalMessageListeners.remove(id)?.remove()
                }
                snapshot.documents.forEach { conversationDoc ->
                    val participants = conversationDoc.get("participants") as? List<*> ?: return@forEach
                    val otherUid = participants.filterIsInstance<String>().firstOrNull { it != myUid } ?: return@forEach
                    if (globalMessageListeners.containsKey(conversationDoc.id)) return@forEach
                    firestore.collection("users").document(otherUid).get().addOnSuccessListener { userDoc ->
                        val phone = userDoc.getString("phone") ?: return@addOnSuccessListener
                        val name = userDoc.getString("displayName").orEmpty().ifBlank { phone }
                        val avatar = userDoc.getString("avatarData")
                        val online = userDoc.getBoolean("online") ?: false
                        val lastSeen = userDoc.getLong("lastSeen")
                        val contact = Contact(id = otherUid, name = name, number = phone, avatarData = avatar, online = online, lastSeen = lastSeen)
                        val ref = conversationDoc.reference.collection("messages").orderBy("createdAt")
                        var latestConversationMessages: List<ChatMessage> = emptyList()
                        globalMessageListeners[conversationDoc.id] = ref.addSnapshotListener { msgSnap, msgError ->
                            if (msgError != null || msgSnap == null) return@addSnapshotListener
                            val now = System.currentTimeMillis()
                            val remote = msgSnap.documents.mapNotNull { doc ->
                                val sender = doc.getString("senderUid") ?: return@mapNotNull null
                                val created = doc.getLong("createdAt") ?: now
                                if (sender != myUid && doc.getLong("deliveredAt") == null) {
                                    doc.reference.update("deliveredAt", now)
                                }
                                val remoteAttachmentName = doc.getString("attachment")
                                val attachmentData = doc.getString("attachmentData")
                                val chunkCount = (doc.getLong("attachmentChunks") ?: 0L).toInt()
                                val localAttachment = if (chunkCount > 0 && doc.getBoolean("attachmentPending") != true && !remoteAttachmentName.isNullOrBlank()) {
                                    val senderFileExists = sender == myUid && File(filesDir, "private_media/$remoteAttachmentName").exists()
                                    if (senderFileExists) remoteAttachmentName else {
                                    ensureChunkAttachment(doc.reference, doc.id, remoteAttachmentName, chunkCount) { localName ->
                                        latestConversationMessages = latestConversationMessages.map { if (it.remoteId == doc.id) it.copy(attachment = localName) else it }
                                        onBatch(contact, latestConversationMessages)
                                    }
                                    val safeName = remoteAttachmentName.replace(Regex("[^A-Za-z0-9._-]"), "_").take(80).ifBlank { "media" }
                                    "rx_${hash(doc.id).take(20)}_$safeName"
                                    }
                                } else if (!attachmentData.isNullOrBlank() && !remoteAttachmentName.isNullOrBlank()) {
                                    val existing = File(filesDir, "private_media/$remoteAttachmentName")
                                    if (existing.exists()) remoteAttachmentName else saveRemoteAttachment(contact.id, remoteAttachmentName, attachmentData)
                                } else remoteAttachmentName
                                if (doc.getBoolean("attachmentPending") == true) return@mapNotNull null
                                ChatMessage(
                                    id = doc.id.hashCode().toLong(),
                                    contactId = contact.id,
                                    text = doc.getString("text").orEmpty(),
                                    mine = sender == myUid,
                                    time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(created)),
                                    attachment = localAttachment,
                                    remoteId = doc.id,
                                    createdAt = created,
                                    deliveredAt = doc.getLong("deliveredAt"),
                                    readAt = doc.getLong("readAt")
                                )
                            }
                            latestConversationMessages = remote
                            onBatch(contact, remote)
                        }
                    }
                }
            }
    }

    private fun saveRemoteAttachment(contactId: String, fileName: String, encoded: String): String? {
        return runCatching {
            val safeName = "rx_${System.currentTimeMillis()}_${fileName.replace(Regex("[^A-Za-z0-9._-]"), "_")}"
            val file = File(filesDir, "private_media/$safeName")
            file.parentFile?.mkdirs()
            file.writeBytes(Base64.decode(encoded, Base64.DEFAULT))
            if (file.length() > 0) safeName else null
        }.getOrNull()
    }

    private fun sendRemoteAttachment(contact: Contact, fileName: String, onDone: (Boolean) -> Unit = {}) {
        val myUid = auth.currentUser?.uid
        if (myUid.isNullOrBlank()) { onDone(false); return }
        val file = File(filesDir, "private_media/$fileName")
        if (!file.exists() || file.length() <= 0L || file.length() > 7L * 1024L * 1024L) {
            if (file.exists() && file.length() > 7L * 1024L * 1024L) {
                runOnUiThread {
                    android.widget.Toast.makeText(
                        this,
                        "Este vídeo passou de 7 MB. Grave novamente em qualidade econômica ou escolha um vídeo menor.",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            }
            onDone(false)
            return
        }
        resolvePhoneToUid(contact.number, { targetUid ->
            if (targetUid == myUid) { onDone(false); return@resolvePhoneToUid }
            mediaWorkScope.launch {
                val prepared = withContext(Dispatchers.IO) {
                    runCatching {
                        val bytes = file.readBytes()
                        if (bytes.isEmpty()) return@runCatching null
                        // Evita bytes.asList()/chunked(), que cria muitos objetos e deixa o envio pesado.
                        val encodedChunks = ArrayList<String>()
                        var offset = 0
                        val chunkSize = 300_000
                        while (offset < bytes.size) {
                            val end = minOf(offset + chunkSize, bytes.size)
                            encodedChunks.add(Base64.encodeToString(bytes.copyOfRange(offset, end), Base64.NO_WRAP))
                            offset = end
                        }
                        Pair(bytes.size, encodedChunks)
                    }.getOrNull()
                }
                if (prepared == null) { onDone(false); return@launch }
                val (byteSize, encodedChunks) = prepared
                val now = System.currentTimeMillis()
                val conversation = firestore.collection("conversations").document(conversationId(myUid, targetUid))
                val messageRef = conversation.collection("messages").document("media_${hash("$myUid:$fileName:${file.length()}").take(40)}")
                val message = mapOf(
                    "senderUid" to myUid,
                    "recipientUid" to targetUid,
                    "text" to "",
                    "attachment" to fileName,
                    "attachmentChunks" to encodedChunks.size,
                    "attachmentSize" to byteSize,
                    "attachmentPending" to true,
                    "createdAt" to now,
                    "deliveredAt" to null,
                    "readAt" to null
                )
                conversation.set(mapOf("participants" to listOf(myUid, targetUid), "updatedAt" to now), SetOptions.merge())
                    .continueWithTask { messageRef.set(message) }
                    .continueWithTask {
                        val batch = firestore.batch()
                        encodedChunks.forEachIndexed { index, encoded ->
                            batch.set(messageRef.collection("chunks").document(index.toString()), mapOf(
                                "index" to index,
                                "data" to encoded,
                                "senderUid" to myUid
                            ))
                        }
                        batch.commit()
                    }
                    .continueWithTask { messageRef.update("attachmentPending", false) }
                    .addOnSuccessListener { onDone(true) }
                    .addOnFailureListener { onDone(false) }
            }
        }, { onDone(false) })
    }

    private fun ensureChunkAttachment(
        messageRef: com.google.firebase.firestore.DocumentReference,
        messageId: String,
        remoteName: String,
        chunkCount: Int,
        onReady: (String) -> Unit
    ) {
        val safeName = remoteName.replace(Regex("[^A-Za-z0-9._-]"), "_").take(80).ifBlank { "media" }
        val localName = "rx_${hash(messageId).take(20)}_$safeName"
        val target = File(filesDir, "private_media/$localName")
        if (target.exists() && target.length() > 0L) { onReady(localName); return }
        if (!downloadingAttachmentIds.add(messageId)) {
            attachmentReadyCallbacks.getOrPut(messageId) { mutableListOf() }.add(onReady)
            return
        }
        attachmentReadyCallbacks.getOrPut(messageId) { mutableListOf() }.add(onReady)
        messageRef.collection("chunks").orderBy("index").get()
            .addOnSuccessListener { snapshot ->
                mediaWorkScope.launch {
                    val saved = withContext(Dispatchers.IO) {
                        runCatching {
                            if (snapshot.size() != chunkCount) throw IllegalStateException("Mídia incompleta")
                            val output = ByteArrayOutputStream()
                            snapshot.documents.forEach { chunkDoc ->
                                val encoded = chunkDoc.getString("data") ?: throw IllegalStateException("Bloco inválido")
                                output.write(Base64.decode(encoded, Base64.DEFAULT))
                            }
                            target.parentFile?.mkdirs()
                            target.writeBytes(output.toByteArray())
                            target.exists() && target.length() > 0L
                        }.getOrDefault(false)
                    }
                    if (saved) {
                        val callbacks = attachmentReadyCallbacks.remove(messageId).orEmpty().toList()
                        downloadingAttachmentIds.remove(messageId)
                        callbacks.forEach { callback -> callback(localName) }
                    } else {
                        downloadingAttachmentIds.remove(messageId)
                        attachmentReadyCallbacks.remove(messageId)
                    }
                }
            }
            .addOnFailureListener {
                downloadingAttachmentIds.remove(messageId)
                attachmentReadyCallbacks.remove(messageId)
            }
    }

    private fun listenRemoteMessages(contact: Contact, onUpdate: (List<ChatMessage>) -> Unit) {
        messagesListener?.remove()
        messagesListener = null
        val myUid = auth.currentUser?.uid ?: return
        resolvePhoneToUid(contact.number, { targetUid ->
            val ref = firestore.collection("conversations").document(conversationId(myUid, targetUid)).collection("messages")
            var latestRemoteMessages: List<ChatMessage> = emptyList()
            messagesListener = ref.orderBy("createdAt")
                .addSnapshotListener { snapshot, error ->
                    if (error != null || snapshot == null) return@addSnapshotListener
                    val remote = snapshot.documents.mapNotNull { doc ->
                        val sender = doc.getString("senderUid") ?: return@mapNotNull null
                        val created = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        if (sender != myUid && (doc.getLong("deliveredAt") == null || doc.getLong("readAt") == null)) {
                            val now = System.currentTimeMillis()
                            doc.reference.update("deliveredAt", doc.getLong("deliveredAt") ?: now, "readAt", doc.getLong("readAt") ?: now)
                        }
                        val remoteAttachmentName = doc.getString("attachment")
                        val attachmentData = doc.getString("attachmentData")
                        val chunkCount = (doc.getLong("attachmentChunks") ?: 0L).toInt()
                        val localAttachment = if (chunkCount > 0 && doc.getBoolean("attachmentPending") != true && !remoteAttachmentName.isNullOrBlank()) {
                            val senderFileExists = sender == myUid && File(filesDir, "private_media/$remoteAttachmentName").exists()
                            if (senderFileExists) remoteAttachmentName else {
                            ensureChunkAttachment(doc.reference, doc.id, remoteAttachmentName, chunkCount) { localName ->
                                latestRemoteMessages = latestRemoteMessages.map { if (it.remoteId == doc.id) it.copy(attachment = localName) else it }
                                onUpdate(latestRemoteMessages)
                            }
                            val safeName = remoteAttachmentName.replace(Regex("[^A-Za-z0-9._-]"), "_").take(80).ifBlank { "media" }
                            "rx_${hash(doc.id).take(20)}_$safeName"
                            }
                        } else if (!attachmentData.isNullOrBlank() && !remoteAttachmentName.isNullOrBlank()) {
                            val existing = File(filesDir, "private_media/$remoteAttachmentName")
                            if (existing.exists()) remoteAttachmentName else saveRemoteAttachment(contact.id, remoteAttachmentName, attachmentData)
                        } else remoteAttachmentName
                        if (doc.getBoolean("attachmentPending") == true) return@mapNotNull null
                        ChatMessage(
                            id = doc.id.hashCode().toLong(),
                            contactId = contact.id,
                            text = doc.getString("text").orEmpty(),
                            mine = sender == myUid,
                            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(created)),
                            attachment = localAttachment,
                            remoteId = doc.id,
                            createdAt = created,
                            deliveredAt = doc.getLong("deliveredAt"),
                            readAt = doc.getLong("readAt")
                        )
                    }
                    latestRemoteMessages = remote
                    onUpdate(remote)
                }
        }, { })
    }

    private fun normalizePhone(value: String): String {
        return value.filter { it.isDigit() }
    }

    private fun phoneIndexKey(phone: String): String = hash("pdf-phone:$phone")

    private fun registerPhoneOnServer(phoneInput: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val phone = normalizePhone(phoneInput)
        if (phone.length < 8) {
            onError("Digite um número de telefone válido.")
            return
        }

        fun doRegister() {
            val uid = auth.currentUser?.uid
            if (uid.isNullOrBlank()) {
                onError("O PDF ainda está conectando. Tente novamente.")
                return
            }
            val oldPhone = normalizePhone(prefs.getString("my_phone", "") ?: "")
            val indexRef = firestore.collection("phone_index").document(phoneIndexKey(phone))
            val oldIndexRef = if (oldPhone.isNotBlank() && oldPhone != phone) firestore.collection("phone_index").document(phoneIndexKey(oldPhone)) else null
            val userRef = firestore.collection("users").document(uid)
            firestore.runTransaction { tx ->
                val oldIndexSnapshot = if (oldIndexRef != null) tx.get(oldIndexRef) else null
                // O número novo passa a apontar para este UID; o índice antigo é removido apenas se pertencer a este usuário.
                tx.set(indexRef, mapOf("uid" to uid, "phone" to phone, "updatedAt" to System.currentTimeMillis()), SetOptions.merge())
                tx.set(userRef, mapOf("phone" to phone, "updatedAt" to System.currentTimeMillis()), SetOptions.merge())
                if (oldIndexSnapshot?.getString("uid") == uid && oldIndexRef != null) tx.delete(oldIndexRef)
            }.addOnSuccessListener {
                prefs.edit().putString("my_phone", phone).apply()
                onSuccess()
            }.addOnFailureListener { e ->
                onError(e.message ?: "Não foi possível cadastrar o número.")
            }
        }

        if (auth.currentUser != null) doRegister()
        else auth.signInAnonymously().addOnSuccessListener { doRegister() }.addOnFailureListener { e ->
            onError(e.message ?: "Não foi possível conectar o PDF.")
        }
    }

    private fun saveProfileToServer(nameInput: String, avatarFileName: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrBlank()) {
            onError("O PDF ainda está conectando.")
            return
        }
        val name = nameInput.trim().ifBlank { "Usuário PDF" }.take(60)
        val data = hashMapOf<String, Any>(
            "displayName" to name,
            "updatedAt" to System.currentTimeMillis()
        )
        if (!avatarFileName.isNullOrBlank()) {
            val file = File(filesDir, "private_media/$avatarFileName")
            val encoded = encodeProfileImage(file)
            if (!encoded.isNullOrBlank()) data["avatarData"] = encoded
        }
        firestore.collection("users").document(uid).set(data, SetOptions.merge())
            .addOnSuccessListener {
                prefs.edit().putString("profile_name", name).putBoolean("profile_setup_done", true).apply()
                onSuccess()
            }
            .addOnFailureListener { e -> onError(e.message ?: "Não foi possível salvar o perfil.") }
    }

    private fun encodeProfileImage(file: File): String? {
        if (!file.exists()) return null
        return runCatching {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return null
            val maxSide = 512
            val scale = minOf(1f, maxSide.toFloat() / maxOf(bitmap.width, bitmap.height).toFloat())
            val resized = if (scale < 1f) Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt().coerceAtLeast(1), (bitmap.height * scale).toInt().coerceAtLeast(1), true) else bitmap
            val out = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.JPEG, 72, out)
            if (resized !== bitmap) resized.recycle()
            Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
        }.getOrNull()
    }

    private fun copyProfileImageToPrivateStorage(uri: Uri): String? {
        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val file = File(dir, "profile_${System.currentTimeMillis()}.jpg")
        return try {
            val input = contentResolver.openInputStream(uri) ?: return null
            input.use { source -> file.outputStream().use { output -> source.copyTo(output) } }
            if (file.length() > 0L) file.name else null
        } catch (_: Exception) {
            file.delete()
            null
        }
    }

    private fun prepareProfileCamera(): Uri? {
        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val file = File(dir, "profile_${System.currentTimeMillis()}.jpg")
        profileCameraOutputFile = file
        prefs.edit().putString("profile_camera_pending_file", file.name).commit()
        return runCatching { FileProvider.getUriForFile(this, "${packageName}.fileprovider", file) }.getOrNull()
    }

    private fun resolvePhoneToUid(phoneInput: String, onResolved: (String) -> Unit, onError: (String) -> Unit) {
        val phone = normalizePhone(phoneInput)
        if (phone.length < 8) { onError("Número inválido."); return }
        firestore.collection("phone_index").document(phoneIndexKey(phone)).get()
            .addOnSuccessListener { doc ->
                val uid = doc.getString("uid")
                if (doc.exists() && !uid.isNullOrBlank()) onResolved(uid)
                else onError("Esse número ainda não está cadastrado no PDF.")
            }
            .addOnFailureListener { e -> onError(e.message ?: "Não foi possível localizar o contato.") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannels()
        requestNotificationPermissionIfNeeded()

        unlocked = false

        if (auth.currentUser != null) {
            setupCallManager()
            registerPushToken()
            recordInstallationOnce()
        } else {
            auth.signInAnonymously()
                .addOnSuccessListener {
                    setupCallManager()
                    registerPushToken()
                    recordInstallationOnce()
                }
                .addOnFailureListener { setupCallManager() }
        }

        setContent {
            PDFRoot()
        }
    }

    private fun setupCallManager() {
        if (callManager != null) return
        callManager = PdfCallManager(
            context = this,
            firestore = firestore,
            auth = auth,
            onConnected = {
                runOnUiThread { callConnected = true }
            },
            onDisconnected = {
                runOnUiThread { callConnected = false }
            },
            onRemoteVideo = {
                // O renderer remoto é conectado pela tela de chamada.
            },
            onCallFinished = {
                runOnUiThread {
                    activeCall = null
                    callConnected = false
                    callMuted = false
                    callCameraEnabled = true
                }
            },
            onError = { message ->
                runOnUiThread {
                    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun startIncomingRingtone() {
        if (!activityResumed || !chatScreenVisible) return
        if (incomingRingtone?.isPlaying == true) return
        stopIncomingRingtone()
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: return
        incomingRingtone = runCatching {
            RingtoneManager.getRingtone(applicationContext, uri)?.also { ringtone ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) ringtone.isLooping = true
                ringtone.play()
            }
        }.getOrNull()
    }

    private fun stopIncomingRingtone() {
        runCatching { incomingRingtone?.stop() }
        incomingRingtone = null
    }

    override fun onResume() {
        super.onResume()
        activityResumed = true
        if (chatScreenVisible && incomingCall != null) startIncomingRingtone()
    }

    override fun onPause() {
        activityResumed = false
        stopIncomingRingtone()
        super.onPause()
    }

    override fun onDestroy() {
        activityResumed = false
        chatScreenVisible = false
        stopIncomingRingtone()
        stopIncomingCallListener()
        messagesListener?.remove()
        stopGlobalMessageSync()
        runCatching { recorder?.stop() }
        runCatching { recorder?.release() }
        recorder = null
        super.onDestroy()
    }

    private fun startIncomingCallListener(contacts: List<Contact>) {
        val uid = auth.currentUser?.uid ?: return
        incomingCallListener?.remove()
        incomingCallListener = firestore.collection("calls")
            .whereEqualTo("calleeUid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                for (doc in snapshot.documents) {
                    if (doc.getString("status") != "ringing") {
                        if (incomingCall?.callId == doc.id) {
                            stopIncomingRingtone()
                            incomingCall = null
                        }
                        continue
                    }
                    val offer = doc.getString("offerSdp") ?: continue
                    val callerUid = doc.getString("callerUid") ?: continue
                    val typeName = doc.getString("type") ?: "VOICE"
                    val type = runCatching { CallType.valueOf(typeName) }.getOrDefault(CallType.VOICE)
                    val directName = doc.getString("callerName")
                    if (!directName.isNullOrBlank()) {
                        incomingCall = IncomingCallInfo(doc.id, callerUid, type, directName, offer)
                        startIncomingRingtone()
                    } else {
                        firestore.collection("users").document(callerUid).get().addOnSuccessListener { userDoc ->
                            val name = userDoc.getString("displayName").orEmpty().ifBlank { "Chamada recebida" }
                            incomingCall = IncomingCallInfo(doc.id, callerUid, type, name, offer)
                            startIncomingRingtone()
                        }.addOnFailureListener {
                            incomingCall = IncomingCallInfo(doc.id, callerUid, type, "Chamada recebida", offer)
                            startIncomingRingtone()
                        }
                    }
                    break
                }
            }
    }

    private fun stopIncomingCallListener() {
        incomingCallListener?.remove()
        incomingCallListener = null
    }

    private fun setPresence(online: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        val data = if (online) {
            mapOf("online" to true, "lastSeen" to System.currentTimeMillis())
        } else {
            mapOf("online" to false, "lastSeen" to System.currentTimeMillis())
        }
        firestore.collection("users").document(uid).set(data, SetOptions.merge())
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) setPresence(true)
    }

    override fun onStop() {
        if (!ignoreNextStop) setPresence(false)
        super.onStop()

        // Quando a câmera/galeria/documentos/permissão estão abertos,
        // o PDF não deve ser fechado. Fora desses fluxos, sair da tela
        // remove a tarefa do histórico/Recentes e deixa o app bloqueado.
        if (ignoreNextStop) return

        if (prefs.getString("password_hash", null) != null) {
            stopIncomingCallListener()
            messagesListener?.remove()
            messagesListener = null
            stopGlobalMessageSync()
            callManager?.endCall()
            callManager?.release()
            callManager = null
            activeCall = null
            incomingCall = null
            unlocked = false
            finishAndRemoveTask()
        }
    }

    private fun copyMediaToPrivateStorage(uri: Uri): String? {
        val dir = File(filesDir, "private_media").apply {
            mkdirs()
        }

        val originalName = runCatching {
            contentResolver.query(
                uri,
                arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(
                        android.provider.OpenableColumns.DISPLAY_NAME
                    )
                    if (index >= 0) cursor.getString(index) else null
                } else {
                    null
                }
            }
        }.getOrNull() ?: "midia"

        val safeName = originalName
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .take(80)
            .ifBlank { "midia" }

        val name = "media_${System.currentTimeMillis()}_$safeName"
        val target = File(dir, name)

        return try {
            val input = contentResolver.openInputStream(uri)
                ?: return null

            input.use { source ->
                target.outputStream().use { output ->
                    source.copyTo(output)
                }
            }

            if (target.length() > 0L) name else {
                target.delete()
                null
            }
        } catch (_: Exception) {
            target.delete()
            null
        }
    }

    private fun prepareCameraOutput(extension: String): Uri? {
        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val file = File(dir, "camera_${System.currentTimeMillis()}.$extension")
        cameraOutputFile = file
        return runCatching {
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
        }.getOrNull()
    }

    private fun takePhoto(contactId: String) {
        cameraContactId = contactId
        ignoreNextStop = true

        val uri = prepareCameraOutput("jpg")
        if (uri == null) {
            ignoreNextStop = false
            cameraOutputFile = null
            return
        }

        if (checkSelfPermission(android.Manifest.permission.CAMERA) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            pendingCameraAction = "photo"
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            launchPreparedPhoto()
        }
    }

    private fun takeVideo(contactId: String) {
        cameraContactId = contactId
        ignoreNextStop = true

        val uri = prepareCameraOutput("mp4")
        if (uri == null) {
            ignoreNextStop = false
            cameraOutputFile = null
            return
        }

        if (checkSelfPermission(android.Manifest.permission.CAMERA) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            pendingCameraAction = "video"
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            launchPreparedVideo()
        }
    }

    private fun launchPreparedPhoto() {
        val file = cameraOutputFile
        if (file == null) {
            ignoreNextStop = false
            return
        }

        val uri = runCatching {
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
        }.getOrNull()

        if (uri == null) {
            file.delete()
            cameraOutputFile = null
            ignoreNextStop = false
            return
        }

        runCatching {
            cameraPhotoLauncher.launch(uri)
        }.onFailure {
            file.delete()
            cameraOutputFile = null
            ignoreNextStop = false
        }
    }

    private fun launchPreparedVideo() {
        val file = cameraOutputFile
        if (file == null) {
            ignoreNextStop = false
            return
        }

        val uri = runCatching {
            FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
        }.getOrNull()

        if (uri == null) {
            file.delete()
            cameraOutputFile = null
            ignoreNextStop = false
            return
        }

        val captureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            // O transporte atual usa blocos protegidos no Firestore e aceita até 7 MB.
            // Pedimos à câmera uma gravação econômica para que o vídeo caiba nesse limite.
            putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
            putExtra(MediaStore.EXTRA_SIZE_LIMIT, 6_500_000L)
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            clipData = ClipData.newUri(contentResolver, "PDF video output", uri)
        }
        runCatching {
            cameraVideoLauncher.launch(captureIntent)
        }.onFailure {
            file.delete()
            cameraOutputFile = null
            ignoreNextStop = false
            android.widget.Toast.makeText(this, "Não foi possível abrir a câmera de vídeo.", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun copyDocumentToPrivateStorage(uri: Uri): String? {
        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val originalName = runCatching {
            contentResolver.query(
                uri,
                arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
                null, null, null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) cursor.getString(index) else null
                } else null
            }
        }.getOrNull() ?: "documento"
        val safeName = originalName
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .take(100)
            .ifBlank { "documento" }
        val name = "doc_${System.currentTimeMillis()}_$safeName"
        val target = File(dir, name)
        return try {
            val input = contentResolver.openInputStream(uri) ?: return null
            input.use { source -> target.outputStream().use { output -> source.copyTo(output) } }
            if (target.length() > 0L) name else null
        } catch (_: Exception) {
            target.delete()
            null
        }
    }

    private fun requestDocument(contactId: String) {
        documentContactId = contactId
        ignoreNextStop = true
        documentPicker.launch(arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/plain", "application/zip", "*/*"))
    }

    private fun requestLocation(contactId: String) {
        locationContactId = contactId

        if (Build.VERSION.SDK_INT >= 23) {
            val fine =
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED
            val coarse =
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!fine && !coarse) {
                ignoreNextStop = true
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                return
            }
        }

        createLocationAttachment(contactId)
    }

    @SuppressLint("MissingPermission")
    private fun createLocationAttachment(contactId: String) {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        var best: Location? = null
        for (provider in providers) {
            val enabled = runCatching { manager.isProviderEnabled(provider) }.getOrDefault(false)
            if (!enabled) continue
            val location = runCatching { manager.getLastKnownLocation(provider) }.getOrNull()
            if (location != null && (best == null || location.accuracy < best!!.accuracy)) best = location
        }
        if (best != null) {
            saveLocationAttachment(contactId, best!!.latitude, best!!.longitude)
            return
        }
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                saveLocationAttachment(contactId, location.latitude, location.longitude)
                runCatching { manager.removeUpdates(this) }
            }
        }
        for (provider in providers) {
            val enabled = runCatching { manager.isProviderEnabled(provider) }.getOrDefault(false)
            if (enabled) runCatching { manager.requestLocationUpdates(provider, 1000L, 1f, listener) }
        }
    }

    private fun saveLocationAttachment(contactId: String, latitude: Double, longitude: Double) {
        val dir = File(filesDir, "private_media").apply { mkdirs() }
        val name = "location_${System.currentTimeMillis()}.location"
        val file = File(dir, name)
        runCatching { file.writeText("$latitude,$longitude") }
        if (file.exists() && file.length() > 0L) {
            pendingMedia = PendingMedia(contactId, name)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        val soundChannel = NotificationChannel(
            "pdf_messages_v2", "Notificações do PDF", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Avisos de mensagens e mídias do PDF"
            setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            )
            enableVibration(false)
        }
        val silentCallChannel = NotificationChannel(
            "pdf_calls_silent_v2", "Chamadas recebidas", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Avisos silenciosos de chamadas recebidas"
            setSound(null, null)
            enableVibration(false)
            setShowBadge(true)
        }
        manager.createNotificationChannel(soundChannel)
        manager.createNotificationChannel(silentCallChannel)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 4102)
        }
    }

    /**
     * Conta uma instalação por UID anônimo do Firebase, sem gravar nome ou telefone.
     * O documento marcador não pode ser lido por outros usuários; somente o total é público
     * para usuários autenticados do PDF.
     */
    private fun recordInstallationOnce() {
        val uid = auth.currentUser?.uid ?: return
        val markerRef = firestore.collection("download_installations").document(uid)
        val statsRef = firestore.collection("download_stats").document("global")

        firestore.runTransaction { transaction ->
            val marker = transaction.get(markerRef)
            if (!marker.exists()) {
                val stats = transaction.get(statsRef)
                val currentCount = stats.getLong("count") ?: 0L
                transaction.set(
                    markerRef,
                    mapOf("uid" to uid, "createdAt" to System.currentTimeMillis())
                )
                transaction.set(
                    statsRef,
                    mapOf("count" to currentCount + 1L),
                    SetOptions.merge()
                )
            }
        }.addOnFailureListener { error ->
            android.util.Log.w("PDF", "Não foi possível atualizar a contagem de instalações.", error)
        }
    }

    private fun registerPushToken() {
        val uid = auth.currentUser?.uid ?: return
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                firestore.collection("users").document(uid).set(
                    mapOf("fcmToken" to token, "pushEnabled" to true), SetOptions.merge()
                )
            }
    }

    private fun notifyPDF() {
        if (!prefs.getBoolean("setting_notificacoes", true)) return
        postPrivateNotification("message", "Nova notificação", "Você recebeu uma nova notificação.")
    }

    internal fun postPrivateNotification(kind: String, title: String, body: String) {
        val channelId = if (kind == "call") "pdf_calls_silent_v2" else "pdf_messages_v2"
        val manager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_pdf)
            .setContentTitle(title)
            .setContentText(body)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setCategory(if (kind == "call") NotificationCompat.CATEGORY_CALL else NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(if (kind == "call") NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            // Deliberadamente sem PendingIntent: tocar na notificação não abre o PDF.
            .build()
        if (Build.VERSION.SDK_INT < 33 ||
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            manager.notify((System.currentTimeMillis() % 100000).toInt(), notification)
        }
    }

    private fun hash(value: String): String {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(value.toByteArray())
            .joinToString("") {
                "%02x".format(it)
            }
    }

    fun hasPassword(): Boolean {
        return prefs.getString("password_hash", null) != null
    }

    /*
     * IMPORTANTE:
     * commit() é usado aqui de propósito.
     * A senha precisa estar realmente gravada antes de
     * unlocked mudar para true.
     */
    private fun savePassword(value: String) {
        prefs.edit()
            .putString("password_hash", hash(value))
            .commit()
    }

    private fun checkPassword(value: String): Boolean {
        return prefs.getString("password_hash", null) == hash(value)
    }

    private fun isUnlocked(): Boolean {
        return unlocked
    }

    private fun requestMedia(contactId: String) {
        mediaContactId = contactId
        ignoreNextStop = true

        mediaPicker.launch(
            arrayOf(
                "image/*",
                "video/*",
                "audio/*"
            )
        )
    }

    @Composable
    private fun PDFRoot() {
        var tick by remember { mutableStateOf(0) }
        var passwordCreated by remember { mutableStateOf(hasPassword()) }
        var profileReady by remember { mutableStateOf(prefs.getBoolean("profile_setup_done", false) && !prefs.getString("my_phone", null).isNullOrBlank()) }
        var numberRegistered by remember { mutableStateOf(!prefs.getString("my_phone", null).isNullOrBlank()) }

        androidx.compose.runtime.key(tick) {
            androidx.compose.material3.MaterialTheme(
                colorScheme = androidx.compose.material3.darkColorScheme(
                    primary = Accent, background = Bg, surface = SurfaceDark,
                    onBackground = TextMain, onSurface = TextMain,
                    surfaceVariant = Surface2, onSurfaceVariant = TextMuted
                )
            ) {
                when {
                    // PRIMEIRO ACESSO: a senha vem antes de qualquer cadastro.
                    // Depois de criar a senha, o PDF passa para o cadastro do número.
                    !passwordCreated -> PasswordScreen(
                        create = true,
                        onPasswordCreated = { passwordCreated = true; unlocked = true }
                    )
                    // Em todos os acessos seguintes, a senha continua protegendo o app.
                    !isUnlocked() -> PasswordScreen(create = false, onPasswordCreated = null)
                    // Somente depois de desbloquear é que aparecem número e perfil.
                    !numberRegistered -> PhoneSetupScreen(
                        onRegistered = { numberRegistered = true }
                    )
                    !profileReady -> ProfileSetupScreen(
                        onProfileSaved = { profileReady = true }
                    )
                    else -> MessengerScreen(
                        onLock = {
                            stopIncomingCallListener(); callManager?.endCall(); callManager?.release();
                            callManager = null; activeCall = null; incomingCall = null; unlocked = false; tick++
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun PhoneSetupScreen(onRegistered: () -> Unit) {
        var phone by rememberSaveable { mutableStateOf("") }
        var error by rememberSaveable { mutableStateOf("") }
        var saving by rememberSaveable { mutableStateOf(false) }
        Surface(Modifier.fillMaxSize(), color = Bg) {
            Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("PDF", color = Accent, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text("Cadastre seu número", color = TextMain, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.height(22.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it.filter { c -> c.isDigit() || c == '+' || c == ' ' || c == '-' || c == '(' || c == ')' }; error = "" }, label = { Text("Número") }, singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A80), modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                Spacer(Modifier.height(18.dp))
                Button(onClick = { saving = true; registerPhoneOnServer(phone, { saving = false; onRegistered() }, { msg -> saving = false; error = msg }) }, enabled = !saving && normalizePhone(phone).length >= 8, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color(0xFF001E18))) { Text(if (saving) "Cadastrando..." else "Cadastrar número", fontWeight = FontWeight.Bold) }
            }
        }
    }

    @Composable
    private fun ProfileSetupScreen(
        allowPhoneEdit: Boolean = false,
        onProfileSaved: () -> Unit
    ) {
        var name by rememberSaveable { mutableStateOf(prefs.getString("profile_name", "") ?: "") }
        var phone by rememberSaveable { mutableStateOf(prefs.getString("my_phone", "") ?: "") }
        var avatarFile by rememberSaveable { mutableStateOf(prefs.getString("profile_avatar_file", null)) }
        var error by rememberSaveable { mutableStateOf("") }
        var saving by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(profileAvatarFileState) {
            profileAvatarFileState?.let { selectedFile -> avatarFile = selectedFile }
        }
        val effectiveAvatarFile = profileAvatarFileState ?: avatarFile ?: prefs.getString("profile_avatar_file", null)
        val avatarBitmap = remember(effectiveAvatarFile) {
            effectiveAvatarFile?.let {
                runCatching { BitmapFactory.decodeFile(File(filesDir, "private_media/$it").absolutePath) }.getOrNull()
            }
        }
        Surface(Modifier.fillMaxSize(), color = Bg) {
            Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("Seu perfil", color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(18.dp))
                Box(
                    Modifier.size(112.dp).clip(CircleShape).background(Accent).clickable {
                        ignoreNextStop = true
                        profileGalleryLauncher.launch(arrayOf("image/*"))
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarBitmap != null) Image(avatarBitmap.asImageBitmap(), "Foto de perfil", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    else Text(name.take(1).ifBlank { "P" }.uppercase(), color = Color(0xFF001E18), fontSize = 38.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = {
                        val uri = prepareProfileCamera()
                        if (uri != null) {
                            ignoreNextStop = true
                            runCatching { profileCameraLauncher.launch(uri) }.onFailure {
                                ignoreNextStop = false
                                profileCameraOutputFile?.delete()
                                profileCameraOutputFile = null
                                error = "Não foi possível abrir a câmera. Escolha uma foto da galeria."
                            }
                        } else {
                            error = "Não foi possível preparar a câmera. Escolha uma foto da galeria."
                        }
                    }) { Text("📷 Tirar foto") }
                    TextButton(onClick = {
                        ignoreNextStop = true
                        profileGalleryLauncher.launch(arrayOf("image/*"))
                    }) { Text("🖼️ Escolher foto") }
                }
                OutlinedTextField(value = name, onValueChange = { name = it; error = "" }, label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                if (allowPhoneEdit) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { value -> phone = value.filter { c -> c.isDigit() || c == '+' || c == ' ' || c == '-' || c == '(' || c == ')' }; error = "" },
                        label = { Text("Número de telefone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
                if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A80), modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = {
                        val oldPhone = normalizePhone(prefs.getString("my_phone", "") ?: "")
                        val newPhone = normalizePhone(phone)
                        if (allowPhoneEdit && newPhone.length < 8) {
                            error = "Digite um número válido."
                        } else {
                            saving = true
                            saveProfileToServer(name, effectiveAvatarFile, {
                                if (allowPhoneEdit && newPhone != oldPhone) {
                                    registerPhoneOnServer(phone, {
                                        saving = false
                                        onProfileSaved()
                                    }, { msg -> saving = false; error = msg })
                                } else {
                                    saving = false
                                    onProfileSaved()
                                }
                            }, { msg -> saving = false; error = msg })
                        }
                    },
                    enabled = !saving && name.isNotBlank() && (!allowPhoneEdit || normalizePhone(phone).length >= 8),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color(0xFF001E18))
                ) { Text(if (saving) "Salvando..." else if (allowPhoneEdit) "Salvar alterações" else "Continuar", fontWeight = FontWeight.Bold) }
                if (allowPhoneEdit) {
                    TextButton(onClick = onProfileSaved, modifier = Modifier.fillMaxWidth()) { Text("Cancelar") }
                }
            }
        }
    }

    @Composable
    private fun PasswordScreen(
        create: Boolean,
        onPasswordCreated: (() -> Unit)?
    ) {

        var password by rememberSaveable {
            mutableStateOf("")
        }

        var error by rememberSaveable {
            mutableStateOf("")
        }
        var recovering by rememberSaveable { mutableStateOf(false) }
        var recoveryCode by rememberSaveable { mutableStateOf("") }
        var resetAllowed by rememberSaveable { mutableStateOf(false) }

        val valid = password.length >= 4
        val recoveryExpected = normalizePhone(prefs.getString("my_phone", "") ?: "").takeLast(4)

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Bg
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(
                            Accent,
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "PDF",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    Modifier.height(28.dp)
                )

                Text(
                    "PDF",
                    color = TextMain,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Text(
                    when {
                        create -> "Crie uma senha"
                        recovering && resetAllowed -> "Crie uma nova senha"
                        recovering -> "Recuperar senha"
                        else -> "Digite sua senha"
                    },
                    color = TextMuted,
                    fontSize = 17.sp
                )

                Spacer(
                    Modifier.height(24.dp)
                )

                if (recovering && !resetAllowed) {
                    OutlinedTextField(
                        value = recoveryCode,
                        onValueChange = { recoveryCode = it.filter(Char::isDigit).take(4); error = "" },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Código: últimos 4 números do telefone") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    )
                } else {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; error = "" },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(if (recovering && resetAllowed) "Nova senha" else "Senha") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }

                Spacer(
                    Modifier.height(8.dp)
                )

                if (create || (recovering && resetAllowed)) {
                    Text(
                        "Use pelo menos 4 caracteres",
                        color = TextMuted,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (recovering && !resetAllowed) {
                    Text(
                        "O código são os quatro últimos números do telefone cadastrado neste aparelho.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )
                }

                if (error.isNotEmpty()) {
                    Text(
                        error,
                        color = Color(0xFFFF8A80),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    )
                }

                Spacer(
                    Modifier.height(20.dp)
                )

                Button(
                    onClick = {
                        when {
                            recovering && !resetAllowed -> {
                                if (recoveryExpected.length != 4) {
                                    error = "Não há um número cadastrado neste aparelho para recuperar a senha."
                                } else if (recoveryCode.length != 4 || recoveryCode != recoveryExpected) {
                                    error = "Código incorreto. Confira os quatro últimos números do telefone."
                                } else {
                                    resetAllowed = true
                                    error = ""
                                    password = ""
                                }
                            }
                            !valid -> error = "Use pelo menos 4 caracteres"
                            create || (recovering && resetAllowed) -> {
                                savePassword(password)
                                if (create) onPasswordCreated?.invoke() else unlocked = true
                            }
                            checkPassword(password) -> unlocked = true
                            else -> error = "Senha incorreta"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = if (recovering && !resetAllowed) recoveryCode.length == 4 else valid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Color(0xFF001E18)
                    )
                ) {

                    Text(
                        when {
                            recovering && !resetAllowed -> "Verificar código"
                            create -> "Criar"
                            recovering && resetAllowed -> "Salvar nova senha"
                            else -> "Entrar"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!create && !recovering) {
                    TextButton(onClick = { recovering = true; resetAllowed = false; recoveryCode = ""; password = ""; error = "" }) {
                        Text("Esqueci a senha", color = Accent)
                    }
                }
                if (recovering) {
                    TextButton(onClick = { recovering = false; resetAllowed = false; recoveryCode = ""; password = ""; error = "" }) {
                        Text("Voltar", color = TextMuted)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MessengerScreen(
        onLock: () -> Unit
    ) {

        var contacts by remember { mutableStateOf(loadContacts()) }
        var messages by remember { mutableStateOf(loadMessages()) }
        var selected by rememberSaveable { mutableStateOf<String?>(null) }
        var showAdd by remember { mutableStateOf(false) }
        var showSettings by remember { mutableStateOf(false) }
        var showEditProfile by remember { mutableStateOf(false) }
        var downloadUrl by remember { mutableStateOf<String?>(null) }
        var editingContact by remember { mutableStateOf<Contact?>(null) }

        // Mantém o link de compartilhamento carregado na tela principal, sem exigir abrir Configurações.
        DisposableEffect(Unit) {
            val linkListener = firestore.collection("app_config").document("download")
                .addSnapshotListener { snapshot, _ ->
                    downloadUrl = snapshot?.getString("url")?.trim()?.takeIf { it.startsWith("https://") }
                }
            onDispose { linkListener.remove() }
        }
        var deletingContact by remember { mutableStateOf<Contact?>(null) }
        var deletingConversation by remember { mutableStateOf<Contact?>(null) }
        var page by rememberSaveable { mutableStateOf(0) } // 0 = Conversas, 1 = Contatos

        if (showEditProfile) {
            ProfileSetupScreen(
                allowPhoneEdit = true,
                onProfileSaved = { showEditProfile = false }
            )
            return
        }

        DisposableEffect(auth.currentUser?.uid) {
            startGlobalMessageSync { incomingContact, remote ->
                val existing = contacts.firstOrNull { it.number == incomingContact.number }
                val actualContact = existing?.copy(
                    name = incomingContact.name,
                    avatarData = incomingContact.avatarData ?: existing.avatarData,
                    online = incomingContact.online,
                    lastSeen = incomingContact.lastSeen
                ) ?: incomingContact
                if (existing == null) {
                    contacts = contacts + incomingContact
                    saveContacts(contacts)
                } else if (actualContact != existing) {
                    contacts = contacts.map { if (it.number == existing.number) actualContact else it }
                    saveContacts(contacts)
                }
                val incomingForContact = remote.map { it.copy(contactId = actualContact.id) }
                val remoteIds = incomingForContact.mapNotNull { it.remoteId }.toSet()
                val remoteAttachmentNames = incomingForContact.filter { it.mine }.mapNotNull { it.attachment }.toSet()
                val localMediaNotYetUploaded = messages.filter {
                    it.contactId == actualContact.id && it.attachment != null && it.attachment !in remoteAttachmentNames
                }
                val localPendingTexts = messages.filter {
                    it.contactId == actualContact.id && it.remoteId != null && it.remoteId !in remoteIds && it.attachment == null
                }
                messages = (messages.filterNot { it.contactId == actualContact.id && (it.remoteId != null || it.attachment != null) } + localMediaNotYetUploaded + localPendingTexts + incomingForContact)
                    .distinctBy { it.remoteId ?: "local:${it.id}" }
                    .sortedBy { it.createdAt }
                saveMessages(messages)
            }
            onDispose { stopGlobalMessageSync() }
        }

        LaunchedEffect(pendingMedia) {
            val media = pendingMedia ?: return@LaunchedEffect
            if (contacts.any { it.id == media.contactId }) {
                var nextId = System.currentTimeMillis()
                val uniqueNames = media.names.distinct().filterNot { name ->
                    messages.any { it.contactId == media.contactId && it.mine && it.attachment == name }
                }
                val newMessages = uniqueNames.map { name ->
                    ChatMessage(
                        id = nextId++,
                        contactId = media.contactId,
                        text = "",
                        mine = true,
                        time = nowTime(),
                        attachment = name
                    )
                }
                if (newMessages.isNotEmpty()) {
                    messages = messages + newMessages
                    saveMessages(messages)
                }
                val mediaContact = contacts.firstOrNull { it.id == media.contactId }
                if (mediaContact == null) {
                    android.widget.Toast.makeText(this@MainActivity, "Contato não encontrado. Os arquivos ficaram salvos neste aparelho.", android.widget.Toast.LENGTH_LONG).show()
                } else {
                    uniqueNames.forEach { fileName ->
                        sendRemoteAttachment(mediaContact, fileName) { ok ->
                            if (!ok) runOnUiThread {
                                android.widget.Toast.makeText(this@MainActivity, "Não foi possível enviar $fileName. O envio gratuito pelo Firestore aceita arquivos de até 7 MB; arquivos maiores ficam salvos somente neste aparelho.", android.widget.Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            pendingMedia = null
        }

        LaunchedEffect(selected) {
            val c = contacts.firstOrNull { it.id == selected }
            if (c == null) {
                messagesListener?.remove()
                messagesListener = null
            } else {
                listenRemoteMessages(c) { remote ->
                    val remoteIds = remote.mapNotNull { it.remoteId }.toSet()
                    val remoteOwnAttachments = remote.filter { it.mine }.mapNotNull { it.attachment }.toSet()
                    val pendingLocalMedia = messages.filter { it.contactId == c.id && it.attachment != null && it.attachment !in remoteOwnAttachments }
                    val pendingLocalTexts = messages.filter { it.contactId == c.id && it.remoteId != null && it.remoteId !in remoteIds && it.attachment == null }
                    val localOnlyTexts = messages.filter { it.contactId == c.id && it.remoteId == null && it.attachment == null }
                    messages = (pendingLocalMedia + pendingLocalTexts + localOnlyTexts + remote).distinctBy { it.remoteId ?: "local:${it.id}" }.sortedBy { it.createdAt }
                    saveMessages(messages)
                }
            }
        }

        LaunchedEffect(selected) {
            chatScreenVisible = selected != null
            if (!chatScreenVisible) stopIncomingRingtone()
            else if (activityResumed && incomingCall != null) startIncomingRingtone()
        }

        LaunchedEffect(contacts.map { it.number }.joinToString("|")) {
            startIncomingCallListener(contacts)
        }

        if (incomingCall != null && activeCall == null) {
            val call = incomingCall!!
            AlertDialog(
                onDismissRequest = {
                    stopIncomingRingtone()
                    callManager?.rejectIncoming(call)
                    incomingCall = null
                },
                title = { Text(if (call.type == CallType.VOICE) "Chamada de voz" else "Chamada de vídeo") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(call.callerName, fontWeight = FontWeight.Bold)
                        Text("Chamada recebida pelo PDF", color = TextMuted)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        requestCallPermissions(call.type) {
                            setupCallManager()
                            stopIncomingRingtone()
                            callManager?.acceptIncoming(call)
                            activeCall = ActiveCallInfo(call.callId, call.callerName, call.type, true)
                            callConnected = false
                            incomingCall = null
                        }
                    }) { Text("Atender") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        stopIncomingRingtone()
                        callManager?.rejectIncoming(call)
                        incomingCall = null
                    }) { Text("Recusar") }
                }
            )
        }

        if (activeCall != null) {
            CallScreen(
                call = activeCall!!,
                connected = callConnected,
                muted = callMuted,
                cameraEnabled = callCameraEnabled,
                manager = callManager,
                onMute = { callMuted = callManager?.toggleMute() ?: callMuted },
                onCamera = { callCameraEnabled = callManager?.toggleCamera() ?: callCameraEnabled },
                onSwitchCamera = { callManager?.switchCamera() },
                onEnd = { callManager?.endCall() }
            )
            return
        }

        if (selected != null) {
            val contact = contacts.firstOrNull { it.id == selected }
            if (contact != null) {
                ChatScreen(
                    contact = contact,
                    contacts = contacts,
                    messages = messages.filter { it.contactId == contact.id },
                    onBack = {
                        messagesListener?.remove()
                        messagesListener = null
                        selected = null
                    },
                    onDeleteConversation = {
                        deletingConversation = contact
                    },
                    onDeleteSelected = { selectedMessages ->
                        val selectedKeys = selectedMessages.map { it.remoteId ?: "local:${it.id}" }.toSet()
                        deletePrivateMediaForMessages(selectedMessages)
                        messages = messages.filterNot {
                            it.contactId == contact.id && (it.remoteId ?: "local:${it.id}") in selectedKeys
                        }
                        saveMessages(messages)
                        val remoteIds = selectedMessages.mapNotNull { it.remoteId }.distinct()
                        if (remoteIds.isNotEmpty()) {
                            val myUid = auth.currentUser?.uid
                            if (!myUid.isNullOrBlank()) {
                                resolvePhoneToUid(contact.number, { targetUid ->
                                    val conversationRef = firestore.collection("conversations")
                                        .document(conversationId(myUid, targetUid))
                                    val batch = firestore.batch()
                                    remoteIds.forEach { remoteId ->
                                        batch.delete(conversationRef.collection("messages").document(remoteId))
                                    }
                                    batch.commit()
                                        .addOnSuccessListener {
                                            android.widget.Toast.makeText(this@MainActivity, "Mensagem(ns) apagada(s).", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            android.widget.Toast.makeText(this@MainActivity, "Firebase recusou a exclusão: ${e.message ?: "permissão negada"}", android.widget.Toast.LENGTH_LONG).show()
                                        }
                                }, { error ->
                                    android.widget.Toast.makeText(this@MainActivity, "Não foi possível localizar a conversa: $error", android.widget.Toast.LENGTH_LONG).show()
                                })
                            }
                        }
                    },
                    onSend = { text, attachment ->
                        val messageId = System.currentTimeMillis()
                        val m = ChatMessage(messageId, contact.id, text, true, nowTime(), attachment,
                            remoteId = if (text.isNotBlank()) messageId.toString() else null)
                        messages = messages + m
                        saveMessages(messages)
                        if (text.isNotBlank()) {
                            sendRemoteText(contact, text, messageId.toString()) { ok ->
                                if (!ok) android.widget.Toast.makeText(this@MainActivity, "Não foi possível entregar a mensagem. Verifique se o número está cadastrado no PDF e tente novamente.", android.widget.Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onCall = {
                        requestCallPermissions(CallType.VOICE) {
                            resolvePhoneToUid(contact.number, { uid ->
                                setupCallManager()
                                val callerName = prefs.getString("profile_name", "Usuário PDF") ?: "Usuário PDF"
                                val callId = callManager?.startOutgoing(uid, CallType.VOICE, callerName)
                                if (callId != null) { callConnected = false; activeCall = ActiveCallInfo(callId, contact.name, CallType.VOICE, false) }
                            }, { msg -> android.widget.Toast.makeText(this@MainActivity, msg, android.widget.Toast.LENGTH_LONG).show() })
                        }
                    },
                    onVideo = {
                        requestCallPermissions(CallType.VIDEO) {
                            resolvePhoneToUid(contact.number, { uid ->
                                setupCallManager()
                                val callerName = prefs.getString("profile_name", "Usuário PDF") ?: "Usuário PDF"
                                val callId = callManager?.startOutgoing(uid, CallType.VIDEO, callerName)
                                if (callId != null) { callConnected = false; activeCall = ActiveCallInfo(callId, contact.name, CallType.VIDEO, false) }
                            }, { msg -> android.widget.Toast.makeText(this@MainActivity, msg, android.widget.Toast.LENGTH_LONG).show() })
                        }
                    },
                    onAttach = { requestMedia(contact.id) },
                    onShareContact = { shared ->
                        val details = "Contato: ${shared.name}\nTelefone: ${shared.number}"
                        val shareId = System.currentTimeMillis()
                        val m = ChatMessage(shareId, contact.id, details, true, nowTime(), null, remoteId = shareId.toString())
                        messages = messages + m
                        saveMessages(messages)
                        sendRemoteText(contact, details, shareId.toString()) { ok ->
                            if (!ok) android.widget.Toast.makeText(this@MainActivity, "Não foi possível enviar o contato.", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    onCamera = { takePhoto(contact.id) },
                    onCameraVideo = { takeVideo(contact.id) },
                    onDocument = { requestDocument(contact.id) },
                    onLocation = { requestLocation(contact.id) },
                    onAudio = {
                        if (isRecording) {
                            val media = stopAudioRecording()
                            if (media != null) {
                                val m = ChatMessage(System.currentTimeMillis(), contact.id, "", true, nowTime(), media.names.firstOrNull())
                                messages = messages + m
                                saveMessages(messages)
                                media.names.firstOrNull()?.let { fileName ->
                                    sendRemoteAttachment(contact, fileName) { ok ->
                                        if (!ok) runOnUiThread {
                                            android.widget.Toast.makeText(this@MainActivity, "O áudio ficou salvo neste aparelho, mas não foi enviado. O envio gratuito pelo Firestore aceita arquivos de até 7 MB.", android.widget.Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                            }
                        } else {
                            startAudioRecording(contact.id)
                        }
                    },
                    isRecording = isRecording
                )
                return
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Bg,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Text("PDF", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, "Configurações")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Bg,
                        titleContentColor = TextMain,
                        actionIconContentColor = TextMain
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAdd = true },
                    containerColor = Accent,
                    contentColor = Color(0xFF001E18)
                ) {
                    Icon(Icons.Default.Add, "Adicionar contato")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedButton(
                    onClick = {
                        val link = downloadUrl
                        if (link.isNullOrBlank()) {
                            android.widget.Toast.makeText(
                                this@MainActivity,
                                "O link oficial do PDF ainda não foi configurado.",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Link do PDF", link))
                            android.widget.Toast.makeText(
                                this@MainActivity,
                                "Link do PDF copiado. Agora é só colar onde quiser.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = Accent
                    )
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Copiar link do PDF", fontWeight = FontWeight.SemiBold)
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { page = 0 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Conversas",
                            color = if (page == 0) Accent else TextMuted,
                            fontWeight = if (page == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    TextButton(
                        onClick = { page = 1 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Contatos",
                            color = if (page == 1) Accent else TextMuted,
                            fontWeight = if (page == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }


                Divider(color = Color(0xFF20262E))

                if (page == 0) {
                    val conversationContacts = contacts.filter { contact ->
                        messages.any { it.contactId == contact.id }
                    }

                    if (conversationContacts.isEmpty()) {
                        EmptySection(
                            title = "Conversas",
                            message = "Quando você iniciar uma conversa, ela aparecerá aqui."
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(conversationContacts, key = { it.id }) { contact ->
                                val last = messages.lastOrNull { it.contactId == contact.id }
                                ContactRow(
                                    contact = contact,
                                    preview = last?.text ?: last?.attachment ?: "",
                                    onClick = { selected = contact.id },
                                    onEdit = { editingContact = contact },
                                    onDelete = { deletingContact = contact },
                                    showContactDelete = false
                                )
                            }
                        }
                    }
                } else {
                    if (contacts.isEmpty()) {
                        EmptySection(
                            title = "Contatos",
                            message = "Adicione um número para começar."
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(contacts, key = { it.id }) { contact ->
                                ContactRow(
                                    contact = contact,
                                    preview = contact.number,
                                    onClick = { selected = contact.id },
                                    onEdit = { editingContact = contact },
                                    onDelete = { deletingContact = contact },
                                    showContactDelete = true
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAdd) {
            AddContactDialog(
                onDismiss = { showAdd = false },
                onSave = { name, number ->
                    val cleanNumber = normalizePhone(number)
                    val c = Contact(System.currentTimeMillis().toString(), name, cleanNumber)
                    contacts = contacts + c
                    saveContacts(contacts)
                    showAdd = false
                    page = 1
                }
            )
        }

        editingContact?.let { contact ->
            EditContactDialog(
                contact = contact,
                onDismiss = { editingContact = null },
                onSave = { name, number ->
                    val cleanNumber = normalizePhone(number)
                    contacts = contacts.map {
                        if (it.id == contact.id) it.copy(name = name.trim(), number = cleanNumber) else it
                    }
                    saveContacts(contacts)
                    editingContact = null
                }
            )
        }

        deletingContact?.let { contact ->
            AlertDialog(
                onDismissRequest = { deletingContact = null },
                title = { Text("Excluir contato?") },
                text = { Text("O contato ${contact.name} será removido deste aparelho. As conversas também serão apagadas deste aparelho.") },
                confirmButton = {
                    TextButton(onClick = {
                        contacts = contacts.filterNot { it.id == contact.id }
                        messages = messages.filterNot { it.contactId == contact.id }
                        saveContacts(contacts)
                        saveMessages(messages)
                        if (selected == contact.id) selected = null
                        deletingContact = null
                    }) { Text("Excluir") }
                },
                dismissButton = {
                    TextButton(onClick = { deletingContact = null }) { Text("Cancelar") }
                }
            )
        }

        deletingConversation?.let { contact ->
            AlertDialog(
                onDismissRequest = { deletingConversation = null },
                title = { Text("Apagar conversa?") },
                text = { Text("As mensagens desta conversa serão apagadas deste aparelho. O contato continuará salvo.") },
                confirmButton = {
                    TextButton(onClick = {
                        // APAGAR CONVERSA = apaga somente as mensagens desta conversa.
                        // O contato permanece salvo na aba Contatos.
                        messagesListener?.remove()
                        messagesListener = null

                        val removedMessages = messages.filter { it.contactId == contact.id }
                        deletePrivateMediaForMessages(removedMessages)
                        messages = messages.filterNot { it.contactId == contact.id }
                        saveMessages(messages)

                        deletingConversation = null
                        selected = null
                    }) { Text("Apagar") }
                },
                dismissButton = {
                    TextButton(onClick = { deletingConversation = null }) { Text("Cancelar") }
                }
            )
        }

        if (showSettings) {
            SettingsDialog(
                onDismiss = { showSettings = false },
                onEditProfile = {
                    showSettings = false
                    showEditProfile = true
                },
                onLock = {
                    showSettings = false
                    onLock()
                }
            )
        }
    }

    @Composable
    private fun EmptySection(
        title: String,
        message: String
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("PDF", color = Accent, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(title, color = TextMain, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(message, color = TextMuted)
        }
    }

    @Composable
    private fun CallScreen(
        call: ActiveCallInfo,
        connected: Boolean,
        muted: Boolean,
        cameraEnabled: Boolean,
        manager: PdfCallManager?,
        onMute: () -> Unit,
        onCamera: () -> Unit,
        onSwitchCamera: () -> Unit,
        onEnd: () -> Unit
    ) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val isVideo = call.type == CallType.VIDEO

        if (isVideo && manager != null) {
            val localRenderer = remember { SurfaceViewRenderer(context) }
            val remoteRenderer = remember { SurfaceViewRenderer(context) }
            val eglContext = manager.eglContext()

            androidx.compose.runtime.DisposableEffect(eglContext) {
                if (eglContext != null) {
                    localRenderer.init(eglContext, null)
                    remoteRenderer.init(eglContext, null)
                    localRenderer.setMirror(true)
                    localRenderer.setEnableHardwareScaler(true)
                    remoteRenderer.setMirror(false)
                    remoteRenderer.setEnableHardwareScaler(true)
                    manager.attachLocalRenderer(localRenderer)
                    manager.attachRemoteRenderer(remoteRenderer)
                }
                onDispose {
                    manager.attachLocalRenderer(null)
                    manager.attachRemoteRenderer(null)
                    runCatching { localRenderer.release() }
                    runCatching { remoteRenderer.release() }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { remoteRenderer },
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .padding(top = 28.dp, end = 16.dp)
                        .size(120.dp, 170.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.DarkGray)
                ) {
                    AndroidView(
                        factory = { localRenderer },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(call.contactName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (connected) "Conectado" else "Chamando...",
                        color = if (connected) Accent else Color.White,
                        fontSize = 13.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 34.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onMute,
                        modifier = Modifier
                            .size(54.dp)
                            .background(Surface2, CircleShape)
                    ) {
                        Icon(
                            if (muted) Icons.Default.MicOff else Icons.Default.Mic,
                            "Microfone",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onCamera,
                        modifier = Modifier
                            .size(54.dp)
                            .background(Surface2, CircleShape)
                    ) {
                        Icon(
                            if (cameraEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                            "Câmera",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = onSwitchCamera,
                        modifier = Modifier
                            .size(54.dp)
                            .background(Surface2, CircleShape)
                    ) {
                        Icon(Icons.Default.Cameraswitch, "Trocar câmera", tint = Color.White)
                    }

                    IconButton(
                        onClick = onEnd,
                        modifier = Modifier
                            .size(58.dp)
                            .background(Color(0xFFD64A4A), CircleShape)
                    ) {
                        Icon(Icons.Default.CallEnd, "Encerrar", tint = Color.White)
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Bg
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(Accent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            call.contactName.take(1).uppercase(),
                            color = Color(0xFF001E18),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                    Text(call.contactName, color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (connected) "Conectado" else "Chamando...",
                        color = if (connected) Accent else TextMuted
                    )
                    Spacer(Modifier.height(42.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                        IconButton(
                            onClick = onMute,
                            modifier = Modifier
                                .size(60.dp)
                                .background(Surface2, CircleShape)
                        ) {
                            Icon(
                                if (muted) Icons.Default.MicOff else Icons.Default.Mic,
                                "Microfone",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = onEnd,
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFD64A4A), CircleShape)
                        ) {
                            Icon(Icons.Default.CallEnd, "Encerrar", tint = Color.White)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ContactRow(
        contact: Contact,
        preview: String,
        onClick: () -> Unit,
        onEdit: () -> Unit,
        onDelete: () -> Unit,
        showContactDelete: Boolean = true
    ) {
        var menu by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contact.name.take(1).uppercase(),
                    color = Color(0xFF001E18),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(contact.name, color = TextMain, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                Text(preview.ifBlank { contact.number }, color = TextMuted, maxLines = 1)
            }

            Box {
                IconButton(onClick = { menu = !menu }) {
                    Icon(Icons.Default.MoreVert, "Mais opções", tint = TextMuted)
                }
                if (menu) {
                    Surface(color = Surface2, shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(6.dp)) {
                            TextButton(onClick = { menu = false; onEdit() }) {
                                Icon(Icons.Default.Edit, null)
                                Spacer(Modifier.size(8.dp))
                                Text("Editar")
                            }
                            if (showContactDelete) {
                                TextButton(onClick = { menu = false; onDelete() }) {
                                    Icon(Icons.Default.Delete, null)
                                    Spacer(Modifier.size(8.dp))
                                    Text("Excluir contato")
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider(color = Color(0xFF20262E))
    }

    private fun decodeAvatarData(data: String?): Bitmap? {
        if (data.isNullOrBlank()) return null
        return runCatching {
            val bytes = Base64.decode(data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }.getOrNull()
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    private fun ChatScreen(
        contact: Contact,
        contacts: List<Contact>,
        messages: List<ChatMessage>,
        onBack: () -> Unit,
        onDeleteConversation: () -> Unit,
        onDeleteSelected: (List<ChatMessage>) -> Unit,
        onSend: (String, String?) -> Unit,
        onCall: () -> Unit,
        onVideo: () -> Unit,
        onAttach: () -> Unit,
        onShareContact: (Contact) -> Unit,
        onCamera: () -> Unit,
        onCameraVideo: () -> Unit,
        onDocument: () -> Unit,
        onLocation: () -> Unit,
        onAudio: () -> Unit,
        isRecording: Boolean
    ) {

        var text by rememberSaveable {
            mutableStateOf("")
        }

        var showAttachOptions by remember { mutableStateOf(false) }
        var showCameraOptions by remember { mutableStateOf(false) }
        var showContactPicker by remember { mutableStateOf(false) }
        var selectedMessageIds by remember(contact.id) { mutableStateOf(setOf<String>()) }
        var showDeleteSelectedConfirm by remember { mutableStateOf(false) }

        val listState =
            rememberLazyListState()

        LaunchedEffect(messages.size) {

            if (messages.isNotEmpty()) {

                listState.animateScrollToItem(
                    messages.lastIndex
                )
            }
        }

        Scaffold(
            containerColor = Bg,
            contentWindowInsets = WindowInsets.safeDrawing,

            topBar = {

                TopAppBar(

                    navigationIcon = {

                        IconButton(
                            onClick = onBack
                        ) {

                            Icon(
                                Icons.Default.ArrowBack,
                                "Voltar"
                            )
                        }
                    },

                    title = {

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            val avatarBitmap = remember(contact.avatarData) { decodeAvatarData(contact.avatarData) }
                            if (avatarBitmap != null) {
                                Image(
                                    bitmap = avatarBitmap.asImageBitmap(),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.size(40.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Accent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(contact.name.take(1).uppercase(), color = Color(0xFF001E18), fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(
                                Modifier.size(10.dp)
                            )

                            Column {

                                Text(
                                    contact.name,
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                val presenceText = if (contact.online) {
                                    "online"
                                } else if (contact.lastSeen != null) {
                                    "visto por último ${SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(contact.lastSeen))}"
                                } else {
                                    "offline"
                                }
                                Text(
                                    presenceText,
                                    color = if (contact.online) Accent else TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    },

                    actions = {

                        IconButton(
                            onClick = onVideo
                        ) {
                            Icon(
                                Icons.Default.Videocam,
                                "Chamada de vídeo"
                            )
                        }

                        IconButton(
                            onClick = onCall
                        ) {
                            Icon(
                                Icons.Default.Call,
                                "Chamada de voz"
                            )
                        }

                        if (selectedMessageIds.isNotEmpty()) {
                            Text("${selectedMessageIds.size}", color = TextMain, fontSize = 13.sp)
                            IconButton(onClick = { showDeleteSelectedConfirm = true }) {
                                Icon(Icons.Default.Delete, "Excluir mensagens selecionadas", tint = Color(0xFFFF6B6B))
                            }
                            IconButton(onClick = { selectedMessageIds = emptySet() }) {
                                Icon(Icons.Default.Close, "Cancelar seleção")
                            }
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Bg,
                            titleContentColor = TextMain,
                            actionIconContentColor = TextMain
                        )
                )
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                if (messages.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            "Comece a conversa",
                            color = TextMuted
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),

                        state = listState,

                        verticalArrangement =
                            Arrangement.spacedBy(
                                6.dp,
                                Alignment.Bottom
                            ),

                        contentPadding =
                            androidx.compose.foundation.layout.PaddingValues(
                                start = 10.dp,
                                end = 10.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            )
                    ) {

                        items(
                            messages,
                            key = { it.remoteId ?: "local:${it.id}" }
                        ) { m ->

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    if (m.mine)
                                        Arrangement.End
                                    else
                                        Arrangement.Start,

                                verticalAlignment =
                                    Alignment.Bottom
                            ) {

                                Surface(
                                    modifier =
                                        Modifier
                                            .widthIn(
                                                max = if (m.attachment?.let { n ->
                                                        n.lowercase().let { x ->
                                                            x.endsWith(".m4a") || x.endsWith(".mp3") || x.endsWith(".aac") || x.endsWith(".wav") || x.endsWith(".ogg")
                                                        }
                                                    } == true) 250.dp else 320.dp
                                            )
                                            .combinedClickable(
                                                onClick = {
                                                    val messageKey = m.remoteId ?: "local:${m.id}"
                                                    if (selectedMessageIds.isNotEmpty()) {
                                                        selectedMessageIds = if (messageKey in selectedMessageIds) selectedMessageIds - messageKey else selectedMessageIds + messageKey
                                                    }
                                                },
                                                onLongClick = {
                                                    selectedMessageIds = selectedMessageIds + (m.remoteId ?: "local:${m.id}")
                                                }
                                            ),

                                    color =
                                        if ((m.remoteId ?: "local:${m.id}") in selectedMessageIds)
                                            Accent.copy(alpha = 0.35f)
                                        else if (m.mine)
                                            BubbleMine
                                        else
                                            BubbleOther,

                                    shape =
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomEnd =
                                                if (m.mine)
                                                    5.dp
                                                else
                                                    18.dp,
                                            bottomStart =
                                                if (m.mine)
                                                    18.dp
                                                else
                                                    5.dp
                                        )
                                ) {

                                    Row(
                                        modifier =
                                            Modifier.padding(
                                                start = 12.dp,
                                                top = 7.dp,
                                                end = 9.dp,
                                                bottom = 6.dp
                                            ),

                                        verticalAlignment =
                                            Alignment.Bottom
                                    ) {

                                        Column(
                                            modifier =
                                                Modifier.weight(
                                                    1f,
                                                    fill = false
                                                )
                                        ) {

                                            if (m.attachment != null) {
                                                PrivateMediaPreview(
                                                    fileName = m.attachment,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }

                                            if (
                                                m.text.isNotBlank()
                                            ) {

                                                Text(
                                                    m.text,
                                                    color = TextMain,
                                                    fontSize = 15.sp
                                                )
                                            }
                                        }

                                        Spacer(
                                            Modifier.size(7.dp)
                                        )

                                        Text(
                                            m.time,
                                            color = TextMuted,
                                            fontSize = 10.sp,
                                            maxLines = 1
                                        )
                                        if (m.mine) {
                                            Text(
                                                when {
                                                    m.readAt != null -> "✓✓"
                                                    m.deliveredAt != null -> "✓✓"
                                                    else -> "✓"
                                                },
                                                color = if (m.readAt != null) Accent else TextMuted,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (isRecording) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceDark)
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFFF6B6B), CircleShape)
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            "Gravando áudio — toque no botão verde para enviar",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceDark)
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Mensagem", color = TextMuted) },
                        maxLines = 4,
                        shape = RoundedCornerShape(24.dp),
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { showAttachOptions = true }) {
                                    Icon(Icons.Default.AttachFile, "Anexar", tint = TextMain)
                                }
                                IconButton(onClick = { showCameraOptions = true }) {
                                    Icon(Icons.Default.CameraAlt, "Câmera", tint = TextMain)
                                }
                            }
                        }
                    )

                    Spacer(Modifier.size(5.dp))
                    IconButton(
                        onClick = {
                            when {
                                isRecording -> onAudio() // encerra a gravação e envia o áudio
                                text.isNotBlank() -> {
                                    onSend(text.trim(), null)
                                    text = ""
                                }
                                else -> onAudio() // campo vazio: começa a gravar
                            }
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .background(Accent, CircleShape)
                    ) {
                        val showSend = isRecording || text.isNotBlank()
                        Icon(
                            if (showSend) Icons.Default.Send else Icons.Default.Mic,
                            when {
                                isRecording -> "Enviar áudio"
                                text.isNotBlank() -> "Enviar mensagem"
                                else -> "Gravar áudio"
                            },
                            tint = Color(0xFF001E18)
                        )
                    }
                }
            }
        }
        if (showDeleteSelectedConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteSelectedConfirm = false },
                title = { Text("Excluir mensagens?") },
                text = { Text("${selectedMessageIds.size} item(ns) selecionado(s) serão apagados desta conversa neste aparelho. As mensagens remotas selecionadas também serão removidas quando permitido pelo Firebase.") },
                confirmButton = {
                    TextButton(onClick = {
                        val chosen = messages.filter { (it.remoteId ?: "local:${it.id}") in selectedMessageIds }
                        onDeleteSelected(chosen)
                        selectedMessageIds = emptySet()
                        showDeleteSelectedConfirm = false
                    }) { Text("Excluir") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteSelectedConfirm = false }) { Text("Cancelar") }
                }
            )
        }

        if (showCameraOptions) {
            AlertDialog(
                onDismissRequest = { showCameraOptions = false },
                title = { Text("Câmera") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { showCameraOptions = false; onCamera() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CameraAlt, "Tirar foto", tint = Accent)
                            Spacer(Modifier.size(8.dp))
                            Text("Tirar foto")
                        }
                        OutlinedButton(
                            onClick = { showCameraOptions = false; onCameraVideo() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Videocam, "Gravar vídeo", tint = Accent)
                            Spacer(Modifier.size(8.dp))
                            Text("Gravar vídeo")
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showCameraOptions = false }) { Text("Cancelar") } }
            )
        }

        if (showAttachOptions) {
            AlertDialog(
                onDismissRequest = { showAttachOptions = false },
                title = { Text("Anexar") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { showAttachOptions = false; onAttach() }, modifier = Modifier.weight(1f)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.ImageIcon, "Galeria", tint = Accent)
                                    Text("Galeria")
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { showAttachOptions = false; onLocation() }, modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.LocationOn, "Localização", tint = Accent)
                                Spacer(Modifier.size(8.dp))
                                Text("Localização")
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { showAttachOptions = false; showContactPicker = true }, modifier = Modifier.weight(1f)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Person, "Contato", tint = Accent)
                                    Text("Contato")
                                }
                            }
                            OutlinedButton(onClick = { showAttachOptions = false; onDocument() }, modifier = Modifier.weight(1f)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Description, "Documentos", tint = Accent)
                                    Text("Documentos")
                                }
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showAttachOptions = false }) { Text("Fechar") } }
            )
        }
        if (showContactPicker) {
            AlertDialog(
                onDismissRequest = { showContactPicker = false },
                title = { Text("Enviar contato") },
                text = {
                    if (contacts.isEmpty()) Text("Você ainda não tem contatos salvos.")
                    else Column(Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState())) {
                        contacts.forEach { saved ->
                            TextButton(onClick = { showContactPicker = false; onShareContact(saved) }, modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.fillMaxWidth()) {
                                    Text(saved.name, color = TextMain)
                                    Text(saved.number, color = TextMuted, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showContactPicker = false }) { Text("Cancelar") } }
            )
        }
    }

    @Composable
    private fun PrivateMediaPreview(
        fileName: String,
        modifier: Modifier = Modifier
    ) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val file = remember(fileName) {
            File(filesDir, "private_media/$fileName")
        }

        val lower = fileName.lowercase()
        val isVideo = lower.endsWith(".mp4") || lower.endsWith(".3gp") ||
                lower.endsWith(".mkv") || lower.endsWith(".webm") ||
                lower.endsWith(".avi") || lower.endsWith(".mov")
        val isAudio = lower.endsWith(".m4a") || lower.endsWith(".mp3") ||
                lower.endsWith(".aac") || lower.endsWith(".wav") ||
                lower.endsWith(".ogg")
        val isLocation = lower.endsWith(".location")
        val isDocument = lower.startsWith("doc_") || lower.endsWith(".pdf") ||
                lower.endsWith(".doc") || lower.endsWith(".docx") ||
                lower.endsWith(".xls") || lower.endsWith(".xlsx") ||
                lower.endsWith(".txt") || lower.endsWith(".zip")

        var showViewer by remember(fileName) { mutableStateOf(false) }
        var fileReady by remember(fileName) { mutableStateOf(file.exists() && file.length() > 0L) }

        LaunchedEffect(fileName) {
            if (!fileReady) {
                repeat(120) {
                    if (file.exists() && file.length() > 0L) {
                        fileReady = true
                        return@LaunchedEffect
                    }
                    delay(250)
                }
            }
        }

        // Áudio: não abre mais aquela janela de confirmação.
        // O próprio balão vira o player. Ao tocar no botão, começa imediatamente.
        if (isAudio) {
            AudioPlayerInline(file = file)
            return
        }

        val bitmap = remember(fileName, fileReady) {
            if (!fileReady || !file.exists()) {
                null
            } else if (isVideo) {
                runCatching {
                    val retriever = MediaMetadataRetriever()
                    try {
                        retriever.setDataSource(file.absolutePath)
                        // Alguns aparelhos retornam um quadro preto no instante 0.
                        retriever.getFrameAtTime(
                            1_000_000L,
                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                        ) ?: retriever.getFrameAtTime(
                            0L,
                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                        )
                    } finally {
                        runCatching { retriever.release() }
                    }
                }.getOrNull()
            } else if (!isLocation && !isDocument) {
                // Não decodifica a foto em resolução original.
                runCatching {
                    val bounds = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeFile(file.absolutePath, bounds)
                    val maxPreview = 2048
                    var sample = 1
                    while (
                        bounds.outWidth / sample > maxPreview ||
                        bounds.outHeight / sample > maxPreview
                    ) {
                        sample *= 2
                    }
                    val options = BitmapFactory.Options().apply {
                        inSampleSize = sample
                    }
                    BitmapFactory.decodeFile(file.absolutePath, options)
                }.getOrNull()
            } else {
                null
            }
        }

        val durationMs = remember(fileName, fileReady) {
            if (isVideo && fileReady) getMediaDurationMs(file) else 0L
        }

        val previewModifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                if (isLocation) {
                    val parts = runCatching {
                        file.readText().trim().split(",")
                    }.getOrNull()
                    if (parts != null && parts.size == 2) {
                        val uri = Uri.parse(
                            "geo:${parts[0]},${parts[1]}?q=${parts[0]},${parts[1]}"
                        )
                        runCatching {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, uri)
                            )
                        }
                    }
                } else if (isDocument) {
                    openPrivateDocument(context, file)
                } else if (!file.exists() || file.length() <= 0L) {
                    android.widget.Toast.makeText(context, "A mídia ainda está sendo recebida. Aguarde um instante e toque novamente.", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    showViewer = true
                }
            }

        if (bitmap != null) {
            Box(
                modifier = previewModifier
                    .height(210.dp)
                    .background(Surface2),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = if (isVideo) "Vídeo enviado" else "Foto enviada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (isVideo) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xCC000000), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = Color.White, fontSize = 24.sp)
                    }

                    Text(
                        formatDuration(durationMs),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(
                                Color(0xAA000000),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        } else {
            Row(
                modifier = previewModifier
                    .widthIn(max = 320.dp)
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isVideo) "🎥" else if (isLocation) "📍" else if (isDocument) "📄" else "🖼️",
                    fontSize = 24.sp
                )
                Spacer(Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (isVideo) "Vídeo — toque para abrir"
                        else if (isLocation) "Localização compartilhada"
                        else if (isDocument) fileName.removePrefix("doc_").substringAfter('_', fileName.removePrefix("doc_"))
                        else "Foto — toque para abrir",
                        color = TextMain,
                        fontSize = 14.sp
                    )
                    if (isVideo) {
                        Text(
                            formatDuration(durationMs),
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        if (showViewer) {
            if (isVideo) {
                VideoPlayerDialog(file = file, onDismiss = { showViewer = false })
            } else if (!isLocation && !isDocument) {
                PhotoViewerDialog(bitmap = bitmap, onDismiss = { showViewer = false })
            }
        }
    }

    private fun openPrivateDocument(context: Context, file: File) {
        if (!file.exists()) return

        val uri = runCatching {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }.getOrNull() ?: return

        val mime = when (file.extension.lowercase(Locale.ROOT)) {
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "zip" -> "application/zip"
            else -> "application/octet-stream"
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }

        runCatching {
            context.startActivity(Intent.createChooser(intent, "Abrir documento"))
        }
    }

    private fun getMediaDurationMs(file: File): Long {
        if (!file.exists()) return 0L
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )?.toLongOrNull() ?: 0L
        } catch (_: Exception) {
            0L
        } finally {
            runCatching { retriever.release() }
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = (durationMs.coerceAtLeast(0L) / 1000L)
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        return String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
    }

    @Composable
    private fun AudioPlayerInline(file: File) {
        val player = remember(file.path) { android.media.MediaPlayer() }
        var prepared by remember(file.path) { mutableStateOf(false) }
        var playing by remember(file.path) { mutableStateOf(false) }
        var durationMs by remember(file.path) { mutableStateOf(0L) }
        var positionMs by remember(file.path) { mutableStateOf(0L) }

        androidx.compose.runtime.DisposableEffect(file.path) {
            runCatching {
                player.setDataSource(file.absolutePath)
                player.setOnPreparedListener {
                    durationMs = player.duration.toLong().coerceAtLeast(0L)
                    prepared = true
                }
                player.setOnCompletionListener {
                    playing = false
                    positionMs = durationMs
                }
                player.prepare()
                durationMs = player.duration.toLong().coerceAtLeast(0L)
                prepared = true
            }
            onDispose {
                runCatching { player.stop() }
                runCatching { player.release() }
            }
        }

        LaunchedEffect(playing, prepared, player) {
            while (playing && prepared) {
                positionMs = runCatching { player.currentPosition.toLong() }.getOrDefault(positionMs)
                delay(200L)
            }
        }

        val total = durationMs.coerceAtLeast(1L).toFloat()
        val current = positionMs.coerceIn(0L, durationMs.coerceAtLeast(0L)).toFloat()

        Row(
            modifier = Modifier
                .widthIn(max = 190.dp)
                .wrapContentWidth(Alignment.End)
                .background(Surface2, RoundedCornerShape(12.dp))
                .padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                enabled = prepared,
                onClick = {
                    if (!prepared) return@IconButton
                    if (player.isPlaying) {
                        player.pause()
                        playing = false
                    } else {
                        if (positionMs >= durationMs && durationMs > 0L) {
                            player.seekTo(0)
                            positionMs = 0L
                        }
                        player.start()
                        playing = true
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playing) "Pausar áudio" else "Reproduzir áudio",
                    tint = Accent
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Slider(
                    modifier = Modifier.height(24.dp),
                    value = current,
                    onValueChange = { value ->
                        positionMs = value.toLong().coerceIn(0L, durationMs.coerceAtLeast(0L))
                    },
                    onValueChangeFinished = {
                        if (prepared) {
                            player.seekTo(positionMs.toInt())
                        }
                    },
                    valueRange = 0f..total
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatDuration(positionMs),
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    Text(
                        formatDuration(durationMs),
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }

    @Composable
    private fun PhotoViewerDialog(bitmap: Bitmap?, onDismiss: () -> Unit) {
        if (bitmap == null) {
            AlertDialog(onDismissRequest = onDismiss, title = { Text("Foto") }, text = { Text("Não foi possível abrir esta foto.") }, confirmButton = { TextButton(onClick = onDismiss) { Text("Fechar") } })
            return
        }
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            var scale by remember { mutableStateOf(1f) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            offsetX += pan.x
                            offsetY += pan.y
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        },
                    contentScale = ContentScale.Fit
                )
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) { Text("Fechar", color = Color.White) }
            }
        }
    }

    @Composable
    private fun VideoPlayerDialog(file: File, onDismiss: () -> Unit) {
        var surface by remember(file.path) { mutableStateOf<Surface?>(null) }
        var prepared by remember(file.path) { mutableStateOf(false) }
        var playing by remember(file.path) { mutableStateOf(false) }
        var durationMs by remember(file.path) { mutableStateOf(0L) }
        var positionMs by remember(file.path) { mutableStateOf(0L) }

        val player = remember(file.path) {
            android.media.MediaPlayer()
        }

        androidx.compose.runtime.DisposableEffect(file.path) {
            onDispose {
                runCatching { player.stop() }
                runCatching { player.reset() }
                runCatching { player.release() }
                runCatching { surface?.release() }
                surface = null
            }
        }

        LaunchedEffect(surface) {
            val currentSurface = surface ?: return@LaunchedEffect

            prepared = false
            playing = false
            durationMs = 0L
            positionMs = 0L

            runCatching {
                player.reset()
                player.setDataSource(file.absolutePath)
                player.setSurface(currentSurface)

                player.setOnPreparedListener {
                    durationMs = it.duration.toLong().coerceAtLeast(0L)
                    positionMs = 0L
                    prepared = true
                    it.isLooping = false
                    it.start()
                    playing = true
                }

                player.setOnCompletionListener {
                    playing = false
                    positionMs = durationMs
                }

                player.setOnErrorListener { _, _, _ ->
                    prepared = false
                    playing = false
                    durationMs = 0L
                    positionMs = 0L
                    true
                }

                player.prepareAsync()
            }.onFailure {
                prepared = false
                playing = false
            }
        }

        LaunchedEffect(playing, prepared) {
            while (playing && prepared) {
                positionMs = runCatching {
                    player.currentPosition.toLong().coerceIn(
                        0L,
                        durationMs.coerceAtLeast(0L)
                    )
                }.getOrDefault(positionMs)
                kotlinx.coroutines.delay(250L)
            }
        }

        fun seekBy(deltaMs: Long) {
            if (!prepared || durationMs <= 0L) return
            val newPosition = (positionMs + deltaMs)
                .coerceIn(0L, durationMs)

            runCatching {
                player.seekTo(newPosition.toInt())
                positionMs = newPosition
            }
        }

        val safeDuration = durationMs.coerceAtLeast(1L)
        val safePosition = positionMs
            .coerceIn(0L, durationMs.coerceAtLeast(0L))

        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        TextureView(context).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                            )

                            surfaceTextureListener =
                                object : TextureView.SurfaceTextureListener {
                                    override fun onSurfaceTextureAvailable(
                                        texture: android.graphics.SurfaceTexture,
                                        width: Int,
                                        height: Int
                                    ) {
                                        runCatching { surface?.release() }
                                        surface = Surface(texture)
                                    }

                                    override fun onSurfaceTextureSizeChanged(
                                        texture: android.graphics.SurfaceTexture,
                                        width: Int,
                                        height: Int
                                    ) {
                                    }

                                    override fun onSurfaceTextureUpdated(
                                        texture: android.graphics.SurfaceTexture
                                    ) {
                                    }

                                    override fun onSurfaceTextureDestroyed(
                                        texture: android.graphics.SurfaceTexture
                                    ): Boolean {
                                        runCatching { surface?.release() }
                                        surface = null
                                        runCatching { player.pause() }
                                        prepared = false
                                        playing = false
                                        return true
                                    }
                                }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (prepared) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Color(0xCC000000))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Slider(
                            value = safePosition.toFloat(),
                            onValueChange = { value ->
                                positionMs = value.toLong()
                            },
                            onValueChangeFinished = {
                                runCatching {
                                    player.seekTo(positionMs.toInt())
                                }
                            },
                            valueRange = 0f..safeDuration.toFloat(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                formatDuration(positionMs),
                                color = Color.White,
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                formatDuration(durationMs),
                                color = TextMuted,
                                fontSize = 12.sp
                            )

                            Spacer(
                                Modifier
                                    .weight(1f)
                            )

                            TextButton(
                                onClick = { seekBy(-10_000L) },
                                enabled = prepared
                            ) {
                                Text(
                                    "↶ 10s",
                                    color = Color.White
                                )
                            }

                            TextButton(
                                onClick = {
                                    if (player.isPlaying) {
                                        player.pause()
                                        playing = false
                                    } else {
                                        runCatching {
                                            if (positionMs >= durationMs && durationMs > 0L) {
                                                player.seekTo(0)
                                                positionMs = 0L
                                            }
                                            player.start()
                                            playing = true
                                        }
                                    }
                                },
                                enabled = prepared
                            ) {
                                Text(
                                    if (playing) "❚❚" else "▶",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            }

                            TextButton(
                                onClick = { seekBy(10_000L) },
                                enabled = prepared
                            ) {
                                Text(
                                    "10s ↷",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp, end = 10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Fechar", color = Color.White)
                    }
                }
            }
        }
    }

    @Composable
    private fun EditContactDialog(
        contact: Contact,
        onDismiss: () -> Unit,
        onSave: (String, String) -> Unit
    ) {
        var name by rememberSaveable { mutableStateOf(contact.name) }
        var number by rememberSaveable { mutableStateOf(contact.number) }
        var error by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Editar contato") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; error = "" },
                        label = { Text("Nome") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = number,
                        onValueChange = {
                            number = it.filter { c -> c.isDigit() || c == '+' || c == ' ' || c == '-' || c == '(' || c == ')' }
                            error = ""
                        },
                        label = { Text("Número de telefone") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    if (error.isNotBlank()) {
                        Text(error, color = Color(0xFFFF8A80), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = name.isNotBlank() && normalizePhone(number).length >= 8,
                    onClick = {
                        if (normalizePhone(number).length < 8) {
                            error = "Digite um número de telefone válido."
                        } else {
                            onSave(name.trim(), number.trim())
                        }
                    }
                ) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        )
    }

    @Composable
    private fun AddContactDialog(
        onDismiss: () -> Unit,
        onSave: (String, String) -> Unit
    ) {

        var name by remember {
            mutableStateOf("")
        }

        var number by remember {
            mutableStateOf("")
        }

        AlertDialog(

            onDismissRequest = onDismiss,

            title = {
                Text("Novo contato")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text("Nome")
                        },
                        singleLine = true
                    )

                    Spacer(
                        Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = number,
                        onValueChange = {
                            number = it
                        },
                        label = {
                            Text("Número de telefone")
                        },
                        singleLine = true
                    )
                }
            },

            confirmButton = {

                TextButton(
                    enabled =
                        name.isNotBlank() &&
                                number.isNotBlank(),

                    onClick = {
                        onSave(
                            name.trim(),
                            number.trim()
                        )
                    }
                ) {

                    Text("Salvar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = onDismiss
                ) {

                    Text("Cancelar")
                }
            }
        )
    }

    @Composable
    private fun SettingsDialog(
        onDismiss: () -> Unit,
        onEditProfile: () -> Unit,
        onLock: () -> Unit
    ) {
        var downloadCount by remember { mutableStateOf<Long?>(null) }

        DisposableEffect(Unit) {
            val countListener = firestore.collection("download_stats").document("global")
                .addSnapshotListener { snapshot, error ->
                    downloadCount = if (error != null || snapshot == null) null
                    else (snapshot.getLong("count") ?: 0L)
                }
            onDispose { countListener.remove() }
        }

        AlertDialog(

            onDismissRequest = onDismiss,

            title = {
                Text("PDF")
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Text("Privacidade e mensagens", fontWeight = FontWeight.Bold)
                    OutlinedButton(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Editar nome, número e foto")
                    }

                    Text("Downloads", fontWeight = FontWeight.Bold)
                    Text(
                        downloadCount?.let {
                            "Downloads: ${NumberFormat.getIntegerInstance(Locale("pt", "BR")).format(it)}"
                        } ?: "Downloads: carregando...",
                        color = Accent,
                        fontWeight = FontWeight.SemiBold
                    )

                    var visto by remember { mutableStateOf(prefs.getBoolean("setting_visto", true)) }
                    var ultimo by remember { mutableStateOf(prefs.getBoolean("setting_ultimo", true)) }
                    var recebido by remember { mutableStateOf(prefs.getBoolean("setting_recebido", true)) }
                    var notificacoes by remember { mutableStateOf(prefs.getBoolean("setting_notificacoes", true)) }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Visto", modifier = Modifier.weight(1f))
                        androidx.compose.material3.Switch(checked = visto, onCheckedChange = { visto = it; prefs.edit().putBoolean("setting_visto", it).apply() })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Visto por último", modifier = Modifier.weight(1f))
                        androidx.compose.material3.Switch(checked = ultimo, onCheckedChange = { ultimo = it; prefs.edit().putBoolean("setting_ultimo", it).apply() })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Confirmação de recebimento", modifier = Modifier.weight(1f))
                        androidx.compose.material3.Switch(checked = recebido, onCheckedChange = { recebido = it; prefs.edit().putBoolean("setting_recebido", it).apply() })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text("Notificações", modifier = Modifier.weight(1f))
                        androidx.compose.material3.Switch(checked = notificacoes, onCheckedChange = { notificacoes = it; prefs.edit().putBoolean("setting_notificacoes", it).apply() })
                    }

                    Text("Fotos, vídeos e áudios ficam no armazenamento privado do PDF e não são adicionados à galeria.", color = TextMuted)

                    Text("Meu número", fontWeight = FontWeight.Bold)
                    Text(prefs.getString("my_phone", "Não cadastrado") ?: "Não cadastrado", color = Accent)
                    Text("Nome: ${prefs.getString("profile_name", "Usuário PDF") ?: "Usuário PDF"}", color = TextMuted)
                    Text("Para adicionar alguém, use somente o número dessa pessoa.", color = TextMuted)
                    Text("Fotos, vídeos e áudios ficam no armazenamento privado do PDF. O envio atual usa o Firestore como transporte e tem limite de 7 MB por arquivo; vídeos maiores precisam ser reduzidos ou enviados por um serviço de armazenamento configurado.", color = TextMuted)
                }
            },

            confirmButton = {

                TextButton(
                    onClick = onDismiss
                ) {

                    Text("Fechar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = onLock
                ) {

                    Text("Bloquear")
                }
            }
        )
    }

    private fun loadContacts(): List<Contact> {

        val raw =
            prefs.getString(
                "contacts",
                "[]"
            ) ?: "[]"

        return try {

            val a = JSONArray(raw)

            List(a.length()) { i ->

                val o =
                    a.getJSONObject(i)

                Contact(
                    o.getString("id"),
                    o.getString("name"),
                    o.getString("number"),
                    o.optBoolean("blocked"),
                    if (o.isNull("avatarData")) null else o.optString("avatarData", null),
                    o.optBoolean("online", false),
                    if (o.has("lastSeen") && !o.isNull("lastSeen")) o.optLong("lastSeen") else null
                )
            }

        } catch (_: Exception) {

            emptyList()
        }
    }

    private fun saveContacts(
        list: List<Contact>
    ) {

        val a = JSONArray()

        list.forEach {

            a.put(
                JSONObject().apply {

                    put("id", it.id)
                    put("name", it.name)
                    put("number", it.number)
                    put("blocked", it.blocked)
                    put("avatarData", it.avatarData ?: JSONObject.NULL)
                    put("online", it.online)
                    put("lastSeen", it.lastSeen ?: JSONObject.NULL)
                }
            )
        }

        prefs.edit()
            .putString(
                "contacts",
                a.toString()
            )
            .apply()
    }

    private fun loadMessages(): List<ChatMessage> {

        val raw =
            prefs.getString(
                "messages",
                "[]"
            ) ?: "[]"

        return try {

            val a = JSONArray(raw)

            List(a.length()) { i ->

                val o =
                    a.getJSONObject(i)

                ChatMessage(
                    o.getLong("id"),
                    o.getString("contactId"),
                    o.getString("text"),
                    o.getBoolean("mine"),
                    o.getString("time"),
                    if (
                        o.isNull("attachment")
                    )
                        null
                    else
                        o.getString("attachment"),
                    if (o.isNull("remoteId")) null else o.optString("remoteId", null),
                    o.optLong("createdAt", o.getLong("id")),
                    if (o.has("deliveredAt") && !o.isNull("deliveredAt")) o.optLong("deliveredAt") else null,
                    if (o.has("readAt") && !o.isNull("readAt")) o.optLong("readAt") else null
                )
            }

        } catch (_: Exception) {

            emptyList()
        }
    }

    private fun deletePrivateMediaForMessages(items: List<ChatMessage>) {
        val dir = File(filesDir, "private_media")
        items.mapNotNull { it.attachment }
            .filter { it.isNotBlank() }
            .forEach { name ->
                runCatching { File(dir, name).delete() }
            }
    }

    private fun saveMessages(
        list: List<ChatMessage>
    ) {

        val a = JSONArray()

        list.takeLast(1000).forEach {

            a.put(
                JSONObject().apply {

                    put("id", it.id)
                    put(
                        "contactId",
                        it.contactId
                    )
                    put(
                        "text",
                        it.text
                    )
                    put(
                        "mine",
                        it.mine
                    )
                    put(
                        "time",
                        it.time
                    )
                    put(
                        "attachment",
                        it.attachment
                            ?: JSONObject.NULL
                    )
                    put("remoteId", it.remoteId ?: JSONObject.NULL)
                    put("createdAt", it.createdAt)
                    put("deliveredAt", it.deliveredAt ?: JSONObject.NULL)
                    put("readAt", it.readAt ?: JSONObject.NULL)
                }
            )
        }

        prefs.edit()
            .putString(
                "messages",
                a.toString()
            )
            .apply()
    }

    private fun nowTime(): String {

        return SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date())
    }
}