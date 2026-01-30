package com.example.firebasebasedchatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: TextInputEditText
    private lateinit var fabSend: FloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initViews()
        setupRecyclerView()
        setupListeners()
        loadSampleMessages()
    }

    private fun initViews() {
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        fabSend = findViewById(R.id.fabSend)
        toolbar = findViewById(R.id.toolbar)
        
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }
    }

    private fun setupListeners() {
        fabSend.setOnClickListener {
            sendMessage()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun sendMessage() {
        val messageText = etMessage.text.toString().trim()
        
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            return
        }

        // UI only - add message to list
        val message = Message(
            text = messageText,
            sender = "You",
            timestamp = System.currentTimeMillis(),
            isSentByCurrentUser = true
        )
        
        messageAdapter.addMessage(message)
        rvMessages.smoothScrollToPosition(messages.size - 1)
        etMessage.text?.clear()
        
        // Simulate receiving a response after 2 seconds
        simulateReceivedMessage()
    }

    private fun simulateReceivedMessage() {
        rvMessages.postDelayed({
            val responses = listOf(
                "That's interesting!",
                "Tell me more",
                "I agree with you",
                "Thanks for sharing!",
                "Got it!"
            )
            
            val receivedMessage = Message(
                text = responses.random(),
                sender = "Friend",
                timestamp = System.currentTimeMillis(),
                isSentByCurrentUser = false
            )
            
            messageAdapter.addMessage(receivedMessage)
            rvMessages.smoothScrollToPosition(messages.size - 1)
        }, 2000)
    }

    private fun loadSampleMessages() {
        // Add some sample messages for UI demonstration
        val sampleMessages = listOf(
            Message("Hey! Welcome to the chat", "Friend", System.currentTimeMillis() - 300000, false),
            Message("Hi! Thanks for having me", "You", System.currentTimeMillis() - 240000, true),
            Message("How are you doing today?", "Friend", System.currentTimeMillis() - 180000, false),
            Message("I'm doing great! Just working on my Firebase project", "You", System.currentTimeMillis() - 120000, true),
            Message("That sounds awesome! Good luck with it!", "Friend", System.currentTimeMillis() - 60000, false)
        )
        
        messages.addAll(sampleMessages)
        messageAdapter.notifyDataSetChanged()
        rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun logout() {
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
