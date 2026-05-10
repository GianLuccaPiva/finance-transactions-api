#!/bin/bash

BASE_URL="http://localhost:8080"
SEPARATOR="=================================================="

printf "\n%s\n" "$SEPARATOR"
printf " Pismo API - Test Runner\n"
printf "%s\n\n" "$SEPARATOR"

printf "Selecione o endpoint para testar:\n"
printf "  1) POST   /accounts\n"
printf "  2) GET    /accounts/{id}\n"
printf "  3) POST   /transactions\n"
printf "  4) GET    /accounts/{id}/transactions\n"
printf "  5) GET    /accounts/{id}/balance\n"
printf "  6) GET    /transactions?page=0&size=10\n"
printf "  7) DELETE /accounts/{id}\n"
printf "  8) Rodar todos\n"
read -p "Opção: " OPTION

run_create_account() {
    read -p "Document number (ex: 12345678901 — exactly 11 digits): " DOCUMENT_NUMBER
    printf "\n%s\n" "$SEPARATOR"
    printf " POST /accounts - Criando conta\n"
    printf "%s\n" "$SEPARATOR"
    curl -s -X POST "$BASE_URL/accounts" \
      -H "Content-Type: application/json" \
      -d "{\"documentNumber\": \"$DOCUMENT_NUMBER\"}"
    printf "\n"
}

run_get_account() {
    read -p "Account ID (ex: 1): " ACCOUNT_ID
    printf "\n%s\n" "$SEPARATOR"
    printf " GET /accounts/$ACCOUNT_ID - Buscando conta por ID\n"
    printf "%s\n" "$SEPARATOR"
    curl -s "$BASE_URL/accounts/$ACCOUNT_ID"
    printf "\n"
}

run_get_transactions_by_account() {
    read -p "Account ID (ex: 1): " ACCOUNT_ID
    printf "\n%s\n" "$SEPARATOR"
    printf " GET /accounts/$ACCOUNT_ID/transactions - Listando transações da conta\n"
    printf "%s\n" "$SEPARATOR"
    curl -s "$BASE_URL/accounts/$ACCOUNT_ID/transactions"
    printf "\n"
}

run_get_balance() {
    read -p "Account ID (ex: 1): " ACCOUNT_ID
    printf "\n%s\n" "$SEPARATOR"
    printf " GET /accounts/$ACCOUNT_ID/balance - Consultando saldo da conta\n"
    printf "%s\n" "$SEPARATOR"
    curl -s "$BASE_URL/accounts/$ACCOUNT_ID/balance"
    printf "\n"
}

run_get_transactions_paginated() {
    read -p "Page (ex: 0): " PAGE
    read -p "Size (ex: 10): " SIZE
    printf "\n%s\n" "$SEPARATOR"
    printf " GET /transactions?page=$PAGE&size=$SIZE - Listando transações com paginação\n"
    printf "%s\n" "$SEPARATOR"
    curl -s "$BASE_URL/transactions?page=$PAGE&size=$SIZE"
    printf "\n"
}

run_create_transaction() {
    read -p "Account ID (ex: 1): " ACCOUNT_ID
    printf "Operation types: 1=PURCHASE, 2=INSTALLMENT PURCHASE, 3=WITHDRAWAL, 4=PAYMENT\n"
    read -p "Operation type ID (1, 2, 3 or 4): " OPERATION_TYPE_ID
    printf "Amount rules: types 1, 2, 3 require negative (ex: -50.00) | type 4 requires positive (ex: 50.00)\n"
    read -p "Amount: " AMOUNT
    printf "\n%s\n" "$SEPARATOR"
    printf " POST /transactions - Criando transação\n"
    printf "%s\n" "$SEPARATOR"
    curl -s -X POST "$BASE_URL/transactions" \
      -H "Content-Type: application/json" \
      -d "{\"accountId\": $ACCOUNT_ID, \"operationTypeId\": $OPERATION_TYPE_ID, \"amount\": $AMOUNT}"
    printf "\n"
}

run_delete_account() {
    read -p "Account ID (ex: 1): " ACCOUNT_ID
    printf "\n%s\n" "$SEPARATOR"
    printf " DELETE /accounts/$ACCOUNT_ID - Desativando conta\n"
    printf "%s\n" "$SEPARATOR"
    curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" -X DELETE "$BASE_URL/accounts/$ACCOUNT_ID"
}

case $OPTION in
    1) run_create_account ;;
    2) run_get_account ;;
    3) run_create_transaction ;;
    4) run_get_transactions_by_account ;;
    5) run_get_balance ;;
    6) run_get_transactions_paginated ;;
    7) run_delete_account ;;
    8)
        run_create_account
        run_get_account
        run_create_transaction
        run_get_transactions_by_account
        run_get_balance
        run_get_transactions_paginated
        run_delete_account
        ;;
    *) printf "Opção inválida.\n" ;;
esac
