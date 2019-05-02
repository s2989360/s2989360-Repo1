# Use Case Introduction

In this tutorial implementation we will design a solution of the Robotic RPA use case which uses number of Pre-packaged ODF components. Scope of automation includes reading new emails from GMail, parsing attached Excel files to extract Products and enter into InvoicePlane web application. Proper business exception handling using Manual Task is in scope.
Implement the applications communication sequence in the following way:

## Mail Connector

Configure GMail IMAP folder to be source of email messages. Processed messages should be marked and skipped during next execution.

## InvoicePlane

Log in to the InvoicePlane web application, navigate to "Create Product" page and enter new Product. If Product form can't be saved, exception must be captured in user-friendly manner (user should understand cause of issue).

## Transactions data

Results of Excel parsing as well as status of products entering should accumulate for all executions and be available in Transactions data store

## Excel

Attached .xlsx contain single sheet with columns: family | sku | name | description | price | tax

# Design Requirements

1. Use local Control Tower integration with remote Nexus to test developed solution
2. All dependencies must be taken from the ODF Nexus REPOSITORY - https://repository.workfusion.com/content/repositories/wf-dependencies
3. Design RPA code by following RPA guides
4. Use the Split-Join pre-packaged component
5. Store apps credentials in Secret Volt
6. From the performance perspective, it does make sense to parallel execution of the Product Creation bot

# Shared Components

Use separate Git repository for system automated.

Idea is that other teams within your company may reuse RPA code you create to automate InvoicePlane application. 
