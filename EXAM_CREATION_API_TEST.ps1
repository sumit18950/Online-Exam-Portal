#!/usr/bin/env pwsh
# ============================================
# Exam Management API Test Script
# ============================================
# This script tests the complete exam creation and management workflow

param(
    [string]$BaseUrl = "http://localhost:9091/api",
    [string]$Username = "teacher_john",
    [string]$Email = "teacher.john@example.com",
    [string]$Password = "Teacher@123"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   EXAM MANAGEMENT - API TEST" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Base URL: $BaseUrl" -ForegroundColor Yellow
Write-Host ""

# ============================================
# STEP 1: Register Teacher User
# ============================================
Write-Host "STEP 1: Register Teacher User" -ForegroundColor Yellow
Write-Host "-" * 40

$registerPayload = @{
    username = $Username
    email = $Email
    password = $Password
    role = "TEACHER"
} | ConvertTo-Json

Write-Host "Payload:" -ForegroundColor Gray
Write-Host $registerPayload -ForegroundColor Gray
Write-Host ""

try {
    $registerResponse = Invoke-WebRequest -Uri "$BaseUrl/auth/register" `
        -Method Post `
        -Headers @{"Content-Type"="application/json"} `
        -Body $registerPayload `
        -ErrorAction Stop

    $user = $registerResponse.Content | ConvertFrom-Json
    Write-Host "[SUCCESS] Teacher registered successfully!" -ForegroundColor Green
    Write-Host "  ID: $($user.id), Email: $($user.email), Role: $($user.role.roleName)" -ForegroundColor Gray
} catch {
    $code = $_.Exception.Response.StatusCode.Value__
    if ($code -eq 409) {
        Write-Host "[WARNING] User already exists (409 Conflict) - Continuing with login..." -ForegroundColor Yellow
    } else {
        Write-Host "[ERROR] Registration failed: $code" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""

# ============================================
# STEP 2: Login and Get JWT Token
# ============================================
Write-Host "STEP 2: Login and Get JWT Token" -ForegroundColor Yellow
Write-Host "-" * 40

$loginPayload = @{
    email = $Email
    password = $Password
} | ConvertTo-Json

Write-Host "Payload:" -ForegroundColor Gray
Write-Host $loginPayload -ForegroundColor Gray
Write-Host ""

try {
    $loginResponse = Invoke-WebRequest -Uri "$BaseUrl/auth/login" `
        -Method Post `
        -Headers @{"Content-Type"="application/json"} `
        -Body $loginPayload `
        -ErrorAction Stop

    $token = $loginResponse.Content.Trim().Replace('"', '')
    Write-Host "[SUCCESS] Login successful!" -ForegroundColor Green
    Write-Host "  Token: $($token.Substring(0, 30))..." -ForegroundColor Gray
} catch {
    Write-Host "[ERROR] Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ============================================
# STEP 3: Create Subject
# ============================================
Write-Host "STEP 3: Create Subject" -ForegroundColor Yellow
Write-Host "-" * 40

$subjectPayload = @{
    subjectName = "Java Programming 2026"
    description = "Comprehensive Java course covering OOP, Collections, Streams, and Design Patterns"
} | ConvertTo-Json

Write-Host "Payload:" -ForegroundColor Gray
Write-Host $subjectPayload -ForegroundColor Gray
Write-Host ""

try {
    $subjectResponse = Invoke-WebRequest -Uri "$BaseUrl/management/subjects" `
        -Method Post `
        -Headers @{
            "Content-Type" = "application/json"
            "Authorization" = "Bearer $token"
        } `
        -Body $subjectPayload `
        -ErrorAction Stop

    $subject = $subjectResponse.Content | ConvertFrom-Json
    $subjectId = $subject.id
    Write-Host "[SUCCESS] Subject created successfully!" -ForegroundColor Green
    Write-Host "  ID: $subjectId" -ForegroundColor Gray
    Write-Host "  Name: $($subject.subjectName)" -ForegroundColor Gray
} catch {
    Write-Host "[ERROR] Subject creation failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ============================================
# STEP 4: Create Exam
# ============================================
Write-Host "STEP 4: Create Exam" -ForegroundColor Yellow
Write-Host "-" * 40

$examPayload = @{
    examTitle = "Java Midterm Exam - Q1 2026"
    examDate = "2026-04-20"
    examTime = "10:00:00"
    durationMinutes = 120
} | ConvertTo-Json

Write-Host "Payload:" -ForegroundColor Gray
Write-Host $examPayload -ForegroundColor Gray
Write-Host ""

try {
    $examResponse = Invoke-WebRequest -Uri "$BaseUrl/management/exams/$subjectId" `
        -Method Post `
        -Headers @{
            "Content-Type" = "application/json"
            "Authorization" = "Bearer $token"
        } `
        -Body $examPayload `
        -ErrorAction Stop

    $exam = $examResponse.Content | ConvertFrom-Json
    $examId = $exam.id
    Write-Host "[SUCCESS] Exam created successfully!" -ForegroundColor Green
    Write-Host "  ID: $examId" -ForegroundColor Gray
    Write-Host "  Title: $($exam.examTitle)" -ForegroundColor Gray
    Write-Host "  Date: $($exam.examDate)" -ForegroundColor Gray
    Write-Host "  Time: $($exam.examTime)" -ForegroundColor Gray
    Write-Host "  Duration: $($exam.durationMinutes) minutes" -ForegroundColor Gray
} catch {
    Write-Host "[ERROR] Exam creation failed: $($_.Exception.Message)" -ForegroundColor Red
    $code = $_.Exception.Response.StatusCode.Value__
    Write-Host "  Status Code: $code" -ForegroundColor Red
    exit 1
}

Write-Host ""

# ============================================
# STEP 5: Get All Exams
# ============================================
Write-Host "STEP 5: Retrieve All Exams" -ForegroundColor Yellow
Write-Host "-" * 40

try {
    $examsResponse = Invoke-WebRequest -Uri "$BaseUrl/management/exams" `
        -Method Get `
        -Headers @{
            "Authorization" = "Bearer $token"
        } `
        -ErrorAction Stop

    $exams = $examsResponse.Content | ConvertFrom-Json
    Write-Host "[SUCCESS] Exams retrieved successfully!" -ForegroundColor Green
    Write-Host "  Total Exams: $($exams.Count)" -ForegroundColor Gray

    if ($exams.Count -gt 0) {
        Write-Host ""
        Write-Host "  Exam Details:" -ForegroundColor Gray
        foreach ($e in $exams) {
            Write-Host "    - ID: $($e.id), Title: $($e.examTitle), Date: $($e.examDate), Duration: $($e.durationMinutes)min" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "[ERROR] Failed to retrieve exams: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# ============================================
# SUMMARY
# ============================================
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   TEST COMPLETE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Summary:" -ForegroundColor Yellow
Write-Host "  Teacher Email: $Email" -ForegroundColor White
Write-Host "  JWT Token: $($token.Substring(0, 30))..." -ForegroundColor White
Write-Host "  Subject ID: $subjectId (Java Programming 2026)" -ForegroundColor White
Write-Host "  Exam ID: $examId (Java Midterm Exam)" -ForegroundColor White
Write-Host ""
Write-Host "API Endpoints Tested:" -ForegroundColor Yellow
Write-Host "  • POST   /api/auth/register" -ForegroundColor Gray
Write-Host "  • POST   /api/auth/login" -ForegroundColor Gray
Write-Host "  • POST   /api/management/subjects" -ForegroundColor Gray
Write-Host "  • POST   /api/management/exams/{subjectId}" -ForegroundColor Gray
Write-Host "  • GET    /api/management/exams" -ForegroundColor Gray
Write-Host ""






